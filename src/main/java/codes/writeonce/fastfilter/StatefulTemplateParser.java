/*
 * Copyright (c) 2015, Alexey Romenskiy, All rights reserved.
 *
 * This file is part of FastFilter library
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package codes.writeonce.fastfilter;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static codes.writeonce.fastfilter.TextPosition.newPosition;
import static java.util.regex.Pattern.DOTALL;

class StatefulTemplateParser {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\\\*)\\$\\{");
    private static final Pattern SPACE_PATTERN = Pattern.compile("^\\s+");
    private static final Pattern END_PATTERN = Pattern.compile("^\\}");
    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("^\\s*(#)?\\s*([a-zA-Z][\\.\\w]*)");
    private static final Pattern IF_NOT_SET_PATTERN = Pattern.compile("^-");
    private static final Pattern IF_EMPTY_PATTERN = Pattern.compile("^:-");
    private static final Pattern IF_SET_PATTERN = Pattern.compile("^\\+");
    private static final Pattern IF_NOT_EMPTY_PATTERN = Pattern.compile("^:\\+");
    private static final Pattern BAREWORDS_PATTERN = Pattern.compile("^[\\.\\w\\s]+");
    private static final Pattern PROPERTY_REF_PATTERN = Pattern.compile("^\\$(#)?([a-zA-Z][\\.\\w]*)");
    private static final Pattern REPLACEMENT_PROPERTY_REF_PATTERN = Pattern.compile(
            "^\\$(?:(\\d+)|(#)?([a-zA-Z][\\.\\w]*)|\\{\\s*(#)?([a-zA-Z][\\.\\w]*)\\s*\\})");
    private static final Pattern SINGLEQUOTED_PATTERN = Pattern.compile("^'([^\\\\']*(?:\\\\.[^\\\\']*)*)'", DOTALL);
    private static final Pattern DOUBLEQUOTED_PATTERN = Pattern.compile("^\"([^\\\\\"]*(?:\\\\.[^\\\\\"]*)*)\"",
            DOTALL);
    private static final Pattern CONVERSION_PATTERN = Pattern.compile("^~\\s*([\\.\\w]+)");
    private static final Pattern REGEX_PATTERN = Pattern.compile("^/(/)?([^\\\\/}]*(?:\\\\.[^\\\\/}]*)*)(/)?", DOTALL);

    private static final Pattern UNESCAPE_PATTERN = Pattern.compile("\\\\(.)", DOTALL);

    private PropertyResolver resolver;
    private CharSequence source;
    private int start;

    public CompiledTemplate parseTemplate(PropertyResolver resolver, CharSequence source)
            throws TemplateEvaluationException, PropertyResolverException {

        this.resolver = resolver;
        this.source = source;
        start = 0;
        return new CompiledTemplate(doParse().build());
    }

    private TemplateResultBuilder doParse() throws TemplateEvaluationException, PropertyResolverException {

        TemplateResultBuilder builder = EmptyTemplateResultBuilder.newInstance();

        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(source);

        while (matcher.find(start)) {

            final String precedingLiteral = source.subSequence(start, matcher.start()).toString();
            builder = builder.concat(LiteralTemplateResultBuilder.newInstance(precedingLiteral));
            start = matcher.end();

            final int backslashCount = matcher.group(1).length();

            final int escapedBackslashCount = backslashCount / 2;
            final String escapedBackslashes = StringUtils.repeat('\\', escapedBackslashCount);
            builder = builder.concat(LiteralTemplateResultBuilder.newInstance(escapedBackslashes));

            final boolean placeholderEscaped = backslashCount % 2 != 0;

            if (placeholderEscaped) {
                final String escapedLiteral = source.subSequence(matcher.end(1), matcher.end()).toString();
                builder = builder.concat(LiteralTemplateResultBuilder.newInstance(escapedLiteral));
            } else {
                builder = builder.concat(parseExpression());
            }
        }

        final String lastLiteral = source.subSequence(start, source.length()).toString();
        return builder.concat(LiteralTemplateResultBuilder.newInstance(lastLiteral));
    }

    private TemplateResultBuilder parseExpression() throws TemplateEvaluationException, PropertyResolverException {

        TemplateResultBuilder builder = parsePropertyName();

        while (start < source.length()) {
            final TextPosition position = position();

            {
                final Matcher m = match(END_PATTERN);
                if (m.find()) {
                    start = m.end();
                    return builder;
                }
            }
            {
                final Matcher m = match(CONVERSION_PATTERN);
                if (m.find()) {
                    final String conversionType = m.group(1);
                    final StringConverter converter = StringConverterFactory.createConversion(conversionType, position);
                    builder = builder.conversion(position, converter);
                    start = m.end();
                    continue;
                }
            }
            {
                final Matcher m = match(REGEX_PATTERN);
                if (m.find()) {
                    start = m.end();
                    final boolean replaceAll = m.group(1) != null;
                    final String patternSource = m.group(2);
                    final TemplateResultBuilder replacement;
                    if (m.group(3) == null) {
                        replacement = EmptyTemplateResultBuilder.newInstance();
                    } else {
                        replacement = parseReplacement();
                    }

                    final Pattern pattern;
                    try {
                        pattern = Pattern.compile(patternSource);
                    } catch (PatternSyntaxException e) {
                        throw new TemplateEvaluationException(newPosition(source, m.start(2)),
                                "Invalid regular expression", e);
                    }

                    if (replaceAll) {
                        builder = builder.replaceAll(position, pattern, replacement.build());
                    } else {
                        builder = builder.replaceFirst(position, pattern, replacement.build());
                    }

                    continue;
                }
            }
            {
                final Matcher m = match(IF_NOT_SET_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.NOT_SET, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(IF_EMPTY_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.EMPTY, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(IF_SET_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.SET, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(IF_NOT_EMPTY_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.NOT_EMPTY, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(SPACE_PATTERN);
                if (m.find()) {
                    start = m.end();
                    continue;
                }
            }

            throw new TemplateEvaluationException(position, "Unable to parse");
        }

        throw new TemplateEvaluationException(position(), "Missed closing bracket");
    }

    private TemplateResultBuilder parseReplacement() throws TemplateEvaluationException, PropertyResolverException {

        TemplateResultBuilder builder = InitTemplateResultBuilder.newInstance();

        final StringBuilder stringBuilder = new StringBuilder();

        while (start < source.length()) {
            final char c = source.charAt(start);
            switch (c) {
                case '\\':
                    if (start + 1 < source.length()) {
                        stringBuilder.append(c).append(source.charAt(start + 1));
                        start += 2;
                    } else {
                        throw new TemplateEvaluationException(position(), "Unable to parse");
                    }
                    break;
                case '/':
                    start++;
                case '}':
                    return builder.concat(LiteralTemplateResultBuilder.newInstance(stringBuilder.toString()));
                case '$':
                    final Matcher m = match(REPLACEMENT_PROPERTY_REF_PATTERN);
                    if (!m.find()) {
                        throw new TemplateEvaluationException(position(), "Unable to parse");
                    }

                    final String digits = m.group(1);
                    if (digits == null) {
                        builder = builder.concat(LiteralTemplateResultBuilder.newInstance(stringBuilder.toString()));
                        stringBuilder.setLength(0);

                        final boolean lengthFlag;
                        final String propertyName;

                        if (m.group(3) == null) {
                            lengthFlag = m.group(4) != null;
                            propertyName = m.group(5);
                        } else {
                            lengthFlag = m.group(2) != null;
                            propertyName = m.group(3);
                        }

                        final TextPosition position = newPosition(source, m.start());
                        builder = builder.concat(getPropertyAccessor(lengthFlag, propertyName, position));
                    } else {
                        stringBuilder.append(c).append(digits);
                    }

                    start = m.end();
                    break;
                default:
                    stringBuilder.append(c);
                    start++;
            }
        }

        throw new TemplateEvaluationException(position(), "Missed closing bracket");
    }

    private TemplateResultBuilder parseConcatenation() throws TemplateEvaluationException, PropertyResolverException {

        TemplateResultBuilder builder = InitTemplateResultBuilder.newInstance();

        while (start < source.length()) {
            final TextPosition position = position();

            {
                final Matcher m = match(END_PATTERN);
                if (m.find()) {
                    return builder;
                }
            }
            {
                final Matcher m = match(BAREWORDS_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.concat(LiteralTemplateResultBuilder.newInstance(m.group()));
                    continue;
                }
            }
            {
                final Matcher m = match(PROPERTY_REF_PATTERN);
                if (m.find()) {
                    start = m.end();
                    final boolean lengthFlag = m.group(1) != null;
                    final String propertyName = m.group(2);
                    builder = builder.concat(getPropertyAccessor(lengthFlag, propertyName, position));
                    continue;
                }
            }
            {
                final Matcher m = match(SINGLEQUOTED_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.concat(LiteralTemplateResultBuilder.newInstance(unescape(m.group(1))));
                    continue;
                }
            }
            {
                final Matcher m = match(DOUBLEQUOTED_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.concat(LiteralTemplateResultBuilder.newInstance(unescape(m.group(1))));
                    continue;
                }
            }
            {
                final Matcher m = match(IF_NOT_SET_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.NOT_SET, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(IF_EMPTY_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.EMPTY, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(IF_SET_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.SET, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(IF_NOT_EMPTY_PATTERN);
                if (m.find()) {
                    start = m.end();
                    builder = builder.condition(Condition.NOT_EMPTY, parseConcatenation());
                    continue;
                }
            }
            {
                final Matcher m = match(SPACE_PATTERN);
                if (m.find()) {
                    start = m.end();
                    continue;
                }
            }

            throw new TemplateEvaluationException(position, "Unable to parse");
        }

        throw new TemplateEvaluationException(position(), "Missed closing bracket");
    }

    private static String unescape(String value) {
        return UNESCAPE_PATTERN.matcher(value).replaceAll("$1");
    }

    private TemplateResultBuilder parsePropertyName() throws TemplateEvaluationException, PropertyResolverException {

        final TextPosition position = position();

        final Matcher m = match(PROPERTY_NAME_PATTERN);
        if (!m.find()) {
            throw new TemplateEvaluationException(position, "Property name not specified correctly");
        }

        final boolean lengthFlag = m.group(1) != null;
        final String propertyName = m.group(2);
        start = m.end();

        return getPropertyAccessor(lengthFlag, propertyName, position);
    }

    private TemplateResultBuilder getPropertyAccessor(boolean lengthFlag, String propertyName, TextPosition position)
            throws PropertyResolverException {

        final PropertyHolder propertyHolder = resolver.getPropertyHolder(propertyName);
        if (propertyHolder.isConstant()) {
            final String value = propertyHolder.getValue();
            if (value == null) {
                return UndefinedTemplateResultBuilder.newInstance(propertyName, position);
            } else {
                if (lengthFlag) {
                    return LiteralTemplateResultBuilder.newInstance(String.valueOf(value.length()));
                } else {
                    return LiteralTemplateResultBuilder.newInstance(value);
                }
            }
        } else {
            if (lengthFlag) {
                return PropertyValueLengthTemplateResultBuilder.newInstance(propertyHolder, propertyName, position);
            } else {
                return PropertyValueTemplateResultBuilder.newInstance(propertyHolder, propertyName, position);
            }
        }
    }

    private TextPosition position() {
        return newPosition(source, start);
    }

    private Matcher match(Pattern pattern) {
        return pattern.matcher(source).region(start, source.length());
    }
}

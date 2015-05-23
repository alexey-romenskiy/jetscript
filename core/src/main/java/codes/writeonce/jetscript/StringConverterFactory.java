/*
 * Copyright (c) 2015, Alexey Romenskiy, All rights reserved.
 *
 * This file is part of JetScript library
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

package codes.writeonce.jetscript;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringEscapeUtils;

final class StringConverterFactory {

    private static final URLCodec URL_CODEC = new URLCodec();

    public static StringConverter createConversion(String conversionType, final TextPosition position)
            throws TemplateEvaluationException {
        switch (conversionType) {
            case "xml":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return StringEscapeUtils.escapeXml10(value);
                    }
                };
            case "java":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return StringEscapeUtils.escapeJava(value);
                    }
                };
            case "javascript":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return StringEscapeUtils.escapeEcmaScript(value);
                    }
                };
            case "csv":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return StringEscapeUtils.escapeCsv(value);
                    }
                };
            case "url":
                return new StringConverter() {
                    @Override
                    public String convert(String value) throws TemplateEvaluationException {
                        try {
                            return URL_CODEC.encode(value);
                        } catch (EncoderException e) {
                            throw new TemplateEvaluationException(position, "Failed to URL-encode: " + value);
                        }
                    }
                };
            case "propval":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return PropertiesCodec.encodePropertyValue(value);
                    }
                };
            case "propname":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return PropertiesCodec.encodePropertyName(value);
                    }
                };
            case "unix":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return value.replace('\\', '/');
                    }
                };
            case "win":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return value.replace('/', '\\');
                    }
                };
            case "uc":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return value.toUpperCase();
                    }
                };
            case "lc":
                return new StringConverter() {
                    @Override
                    public String convert(String value) {
                        return value.toLowerCase();
                    }
                };
            default:
                throw new TemplateEvaluationException(position, "Unsupported conversion operation: " + conversionType);
        }
    }

    private StringConverterFactory() {
        // empty
    }
}

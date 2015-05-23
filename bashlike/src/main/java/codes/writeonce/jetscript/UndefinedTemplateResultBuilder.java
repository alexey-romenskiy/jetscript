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

import java.util.regex.Pattern;

class UndefinedTemplateResultBuilder implements TemplateResultBuilder {

    private final String propertyName;
    private final TextPosition position;

    public static UndefinedTemplateResultBuilder newInstance(String propertyName, TextPosition position) {
        return new UndefinedTemplateResultBuilder(propertyName, position);
    }

    private UndefinedTemplateResultBuilder(String propertyName, TextPosition position) {
        this.propertyName = propertyName;
        this.position = position;
    }

    @Override
    public TemplateResultBuilder concat(TemplateResultBuilder templateResultBuilder)
            throws TemplateEvaluationException {
        return Errors.undefConcat(this);
    }

    @Override
    public TemplateResultBuilder condition(Condition condition, final TemplateResultBuilder templateResultBuilder) {

        return condition.accept(new Condition.Visitor<TemplateResultBuilder, RuntimeException>() {
            @Override
            public TemplateResultBuilder visitNotSet() {
                return templateResultBuilder;
            }

            @Override
            public TemplateResultBuilder visitEmpty() {
                return templateResultBuilder;
            }

            @Override
            public TemplateResultBuilder visitSet() {
                return UndefinedTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visitNotEmpty() {
                return UndefinedTemplateResultBuilder.this;
            }
        });
    }

    @Override
    public TemplateResultBuilder conversion(TextPosition position, StringConverter converter)
            throws TemplateEvaluationException {
        throw new TemplateEvaluationException(position,
                "Unable to convert undefined value of property: " + propertyName);
    }

    @Override
    public TemplateResultBuilder replaceAll(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException {
        return Errors.undefRegexp(position, this);
    }

    @Override
    public TemplateResultBuilder replaceFirst(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException {
        return Errors.undefRegexp(position, this);
    }

    @Override
    public TemplateResult build() throws TemplateEvaluationException {
        throw new TemplateEvaluationException(position,
                "Unable to evaluate undefined value of property: " + propertyName);
    }

    @Override
    public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
        return visitor.visit(this);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public TextPosition getPosition() {
        return position;
    }
}

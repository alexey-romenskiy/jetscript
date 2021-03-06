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

class LiteralTemplateResultBuilder extends StaticValueTemplateResultBuilder {

    private final String value;

    public static TemplateResultBuilder newInstance(String value) {
        if (value.isEmpty()) {
            return EmptyTemplateResultBuilder.newInstance();
        } else {
            return new LiteralTemplateResultBuilder(value);
        }
    }

    private LiteralTemplateResultBuilder(String value) {
        this.value = value;
    }

    @Override
    public TemplateResultBuilder concat(TemplateResultBuilder templateResultBuilder)
            throws TemplateEvaluationException {

        return templateResultBuilder.accept(new Visitor<TemplateResultBuilder, TemplateEvaluationException>() {
            @Override
            public TemplateResultBuilder visit(InitTemplateResultBuilder initTemplateResultBuilder) {
                return LiteralTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visit(UndefinedTemplateResultBuilder undefinedTemplateResultBuilder)
                    throws TemplateEvaluationException {
                return Errors.undefConcat(undefinedTemplateResultBuilder);
            }

            @Override
            public TemplateResultBuilder visit(EmptyTemplateResultBuilder emptyTemplateResultBuilder) {
                return LiteralTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visit(LiteralTemplateResultBuilder literalTemplateResultBuilder) {
                return LiteralTemplateResultBuilder.newInstance(value + literalTemplateResultBuilder.value);
            }

            @Override
            public TemplateResultBuilder visit(MultiTemplateResultBuilder multiTemplateResultBuilder) {
                return MultiTemplateResultBuilder.newInstance(build(), multiTemplateResultBuilder);
            }

            @Override
            public TemplateResultBuilder visit(SingleValueTemplateResultBuilder singleValueTemplateResultBuilder)
                    throws TemplateEvaluationException {
                return MultiTemplateResultBuilder.newInstance(build(), singleValueTemplateResultBuilder.build());
            }
        });
    }

    @Override
    public TemplateResultBuilder condition(Condition condition, final TemplateResultBuilder templateResultBuilder) {

        return condition.accept(new Condition.Visitor<TemplateResultBuilder, RuntimeException>() {
            @Override
            public TemplateResultBuilder visitNotSet() {
                return LiteralTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visitEmpty() {
                return LiteralTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visitSet() {
                return templateResultBuilder;
            }

            @Override
            public TemplateResultBuilder visitNotEmpty() {
                return templateResultBuilder;
            }
        });
    }

    @Override
    public TemplateResult build() {
        return new LiteralTemplateResult(value);
    }

    @Override
    public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
        return visitor.visit(this);
    }
}

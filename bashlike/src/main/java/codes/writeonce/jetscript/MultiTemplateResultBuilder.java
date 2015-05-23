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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MultiTemplateResultBuilder extends DynamicValueTemplateResultBuilder {

    private final List<TemplateResult> appenders;

    static TemplateResultBuilder newInstance(TemplateResult appender1, TemplateResult appender2) {
        return new MultiTemplateResultBuilder(Arrays.asList(appender1, appender2));
    }

    private static TemplateResultBuilder newInstance(MultiTemplateResultBuilder builder, TemplateResult appender) {
        final List<TemplateResult> appenders = new ArrayList<>(builder.appenders.size() + 1);
        appenders.addAll(builder.appenders);
        appenders.add(appender);
        return new MultiTemplateResultBuilder(appenders);
    }

    static TemplateResultBuilder newInstance(TemplateResult appender, MultiTemplateResultBuilder builder) {
        final List<TemplateResult> appenders = new ArrayList<>(builder.appenders.size() + 1);
        appenders.add(appender);
        appenders.addAll(builder.appenders);
        return new MultiTemplateResultBuilder(appenders);
    }

    private static TemplateResultBuilder newInstance(MultiTemplateResultBuilder builder1,
            MultiTemplateResultBuilder builder2) {
        final List<TemplateResult> appenders = new ArrayList<>(builder1.appenders.size() + builder2.appenders.size());
        appenders.addAll(builder1.appenders);
        appenders.addAll(builder2.appenders);
        return new MultiTemplateResultBuilder(appenders);
    }

    private MultiTemplateResultBuilder(List<TemplateResult> appenders) {
        this.appenders = appenders;
    }

    @Override
    public TemplateResultBuilder concat(TemplateResultBuilder templateResultBuilder)
            throws TemplateEvaluationException {

        return templateResultBuilder.accept(new Visitor<TemplateResultBuilder, TemplateEvaluationException>() {
            @Override
            public TemplateResultBuilder visit(InitTemplateResultBuilder initTemplateResultBuilder) {
                return MultiTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visit(UndefinedTemplateResultBuilder undefinedTemplateResultBuilder)
                    throws TemplateEvaluationException {
                return Errors.undefConcat(undefinedTemplateResultBuilder);
            }

            @Override
            public TemplateResultBuilder visit(EmptyTemplateResultBuilder emptyTemplateResultBuilder) {
                return MultiTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visit(LiteralTemplateResultBuilder literalTemplateResultBuilder) {
                return newInstance(MultiTemplateResultBuilder.this, literalTemplateResultBuilder.build());
            }

            @Override
            public TemplateResultBuilder visit(MultiTemplateResultBuilder multiTemplateResultBuilder) {
                return newInstance(MultiTemplateResultBuilder.this, multiTemplateResultBuilder);
            }

            @Override
            public TemplateResultBuilder visit(SingleValueTemplateResultBuilder singleValueTemplateResultBuilder)
                    throws TemplateEvaluationException {
                return newInstance(MultiTemplateResultBuilder.this, singleValueTemplateResultBuilder.build());
            }
        });
    }

    @Override
    public TemplateResult build() throws TemplateEvaluationException {
        return new MultiTemplateResult(appenders.toArray(new TemplateResult[appenders.size()]));
    }

    @Override
    public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
        return visitor.visit(this);
    }
}

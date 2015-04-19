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

abstract class AbstractEmptyTemplateResultBuilder extends StaticValueTemplateResultBuilder {

    @Override
    public TemplateResultBuilder concat(TemplateResultBuilder templateResultBuilder) {
        return templateResultBuilder;
    }

    @Override
    public TemplateResultBuilder condition(Condition condition, final TemplateResultBuilder templateResultBuilder) {

        return condition.accept(new Condition.Visitor<TemplateResultBuilder, RuntimeException>() {
            @Override
            public TemplateResultBuilder visitNotSet() {
                return AbstractEmptyTemplateResultBuilder.this;
            }

            @Override
            public TemplateResultBuilder visitEmpty() {
                return templateResultBuilder;
            }

            @Override
            public TemplateResultBuilder visitSet() {
                return templateResultBuilder;
            }

            @Override
            public TemplateResultBuilder visitNotEmpty() {
                return AbstractEmptyTemplateResultBuilder.this;
            }
        });
    }

    @Override
    public TemplateResult build() {
        return EmptyTemplateResult.newInstance();
    }
}

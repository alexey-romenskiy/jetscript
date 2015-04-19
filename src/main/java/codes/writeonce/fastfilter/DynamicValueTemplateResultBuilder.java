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

import java.util.regex.Pattern;

abstract class DynamicValueTemplateResultBuilder implements TemplateResultBuilder {

    @Override
    public TemplateResultBuilder condition(Condition condition, final TemplateResultBuilder templateResultBuilder)
            throws TemplateEvaluationException {

        return condition.accept(new Condition.Visitor<TemplateResultBuilder, TemplateEvaluationException>() {
            @Override
            public TemplateResultBuilder visitNotSet() throws TemplateEvaluationException {
                return IfNotSetTemplateResultBuilder.newInstance(build(), templateResultBuilder.build());
            }

            @Override
            public TemplateResultBuilder visitEmpty() throws TemplateEvaluationException {
                return IfEmptyTemplateResultBuilder.newInstance(build(), templateResultBuilder.build());
            }

            @Override
            public TemplateResultBuilder visitSet() throws TemplateEvaluationException {
                return IfSetTemplateResultBuilder.newInstance(build(), templateResultBuilder.build());
            }

            @Override
            public TemplateResultBuilder visitNotEmpty() throws TemplateEvaluationException {
                return IfNotEmptyTemplateResultBuilder.newInstance(build(), templateResultBuilder.build());
            }
        });
    }

    @Override
    public TemplateResultBuilder conversion(TextPosition position, StringConverter converter)
            throws TemplateEvaluationException {
        return StringConverterTemplateResultBuilder.newInstance(build(), converter);
    }

    @Override
    public TemplateResultBuilder replaceAll(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException {
        return ReplaceAllTemplateResultBuilder.newInstance(position, build(), pattern, replacement);
    }

    @Override
    public TemplateResultBuilder replaceFirst(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException {
        return ReplaceFirstTemplateResultBuilder.newInstance(position, build(), pattern, replacement);
    }
}

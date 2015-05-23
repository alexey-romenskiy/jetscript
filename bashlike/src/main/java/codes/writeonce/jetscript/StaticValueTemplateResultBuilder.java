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

abstract class StaticValueTemplateResultBuilder implements TemplateResultBuilder {

    @Override
    public TemplateResultBuilder conversion(TextPosition position, StringConverter converter)
            throws TemplateEvaluationException {
        return LiteralTemplateResultBuilder.newInstance(converter.convert(build().getStringValue()));
    }

    @Override
    public TemplateResultBuilder replaceAll(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException {
        final ReplaceAllTemplateResult result = new ReplaceAllTemplateResult(position, build(), pattern, replacement);
        return LiteralTemplateResultBuilder.newInstance(result.getStringValue());
    }

    @Override
    public TemplateResultBuilder replaceFirst(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException {
        final ReplaceFirstTemplateResult result = new ReplaceFirstTemplateResult(position, build(), pattern,
                replacement);
        return LiteralTemplateResultBuilder.newInstance(result.getStringValue());
    }
}

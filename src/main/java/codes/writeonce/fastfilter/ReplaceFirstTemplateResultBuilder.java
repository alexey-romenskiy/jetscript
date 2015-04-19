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

class ReplaceFirstTemplateResultBuilder extends SingleValueTemplateResultBuilder {

    private final TextPosition position;
    private final TemplateResult source;
    private final Pattern pattern;
    private final TemplateResult replacement;

    public static ReplaceFirstTemplateResultBuilder newInstance(TextPosition position, TemplateResult source,
            Pattern pattern, TemplateResult replacement) {
        return new ReplaceFirstTemplateResultBuilder(position, source, pattern, replacement);
    }

    private ReplaceFirstTemplateResultBuilder(TextPosition position, TemplateResult source, Pattern pattern,
            TemplateResult replacement) {
        this.position = position;
        this.source = source;
        this.pattern = pattern;
        this.replacement = replacement;
    }

    @Override
    public TemplateResult build() throws TemplateEvaluationException {
        return new ReplaceFirstTemplateResult(position, source, pattern, replacement);
    }
}

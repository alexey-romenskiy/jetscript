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

class ReplaceFirstTemplateResult extends RegexTemplateResult {

    public ReplaceFirstTemplateResult(TextPosition position, TemplateResult source, Pattern pattern,
            TemplateResult replacement) {
        super(position, source, pattern, replacement);
    }

    @Override
    protected CharSequence getUnsafe() throws TemplateEvaluationException {
        final CharSequence value = source.getCharSequence();
        matcher.reset(value);
        if (!matcher.find()) {
            return value;
        }
        stringBuffer.setLength(0);
        matcher.appendReplacement(stringBuffer, replacement.getStringValue());
        matcher.appendTail(stringBuffer);
        return stringBuffer;
    }
}

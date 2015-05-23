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

class Errors {

    public static TemplateResultBuilder undefConcat(UndefinedTemplateResultBuilder builder)
            throws TemplateEvaluationException {
        throw new TemplateEvaluationException(builder.getPosition(),
                "Unable to concatenate with undefined value of property: " + builder.getPropertyName());
    }

    public static TemplateResultBuilder undefRegexp(TextPosition position, UndefinedTemplateResultBuilder builder)
            throws TemplateEvaluationException {
        throw new TemplateEvaluationException(position,
                "Unable to apply regular expression on undefined value of property: " + builder.getPropertyName());
    }
}

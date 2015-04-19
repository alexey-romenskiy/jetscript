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

interface TemplateResultBuilder {

    TemplateResultBuilder concat(TemplateResultBuilder templateResultBuilder) throws TemplateEvaluationException;

    TemplateResultBuilder condition(Condition condition, TemplateResultBuilder templateResultBuilder)
            throws TemplateEvaluationException;

    TemplateResultBuilder conversion(TextPosition position, StringConverter converter)
            throws TemplateEvaluationException;

    TemplateResultBuilder replaceAll(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException;

    TemplateResultBuilder replaceFirst(TextPosition position, Pattern pattern, TemplateResult replacement)
            throws TemplateEvaluationException;

    TemplateResult build() throws TemplateEvaluationException;

    <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E;

    interface Visitor<V, E extends Throwable> {

        V visit(InitTemplateResultBuilder initTemplateResultBuilder) throws E;

        V visit(UndefinedTemplateResultBuilder undefinedTemplateResultBuilder) throws E;

        V visit(EmptyTemplateResultBuilder emptyTemplateResultBuilder) throws E;

        V visit(LiteralTemplateResultBuilder literalTemplateResultBuilder) throws E;

        V visit(MultiTemplateResultBuilder multiTemplateResultBuilder) throws E;

        V visit(SingleValueTemplateResultBuilder singleValueTemplateResultBuilder) throws E;
    }
}

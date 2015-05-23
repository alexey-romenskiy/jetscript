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

class IfNotEmptyTemplateResultBuilder extends SingleValueTemplateResultBuilder {

    private final TemplateResult first;
    private final TemplateResult second;

    public static IfNotEmptyTemplateResultBuilder newInstance(TemplateResult first, TemplateResult second) {
        return new IfNotEmptyTemplateResultBuilder(first, second);
    }

    private IfNotEmptyTemplateResultBuilder(TemplateResult first, TemplateResult second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public TemplateResult build() throws TemplateEvaluationException {
        return new IfNotEmptyTemplateResult(first, second);
    }
}

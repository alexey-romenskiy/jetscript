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

class StringConverterTemplateResultBuilder extends SingleValueTemplateResultBuilder {

    private final TemplateResult source;
    private final StringConverter stringConverter;

    public static StringConverterTemplateResultBuilder newInstance(TemplateResult source,
            StringConverter stringConverter) {
        return new StringConverterTemplateResultBuilder(source, stringConverter);
    }

    private StringConverterTemplateResultBuilder(TemplateResult source, StringConverter stringConverter) {
        this.source = source;
        this.stringConverter = stringConverter;
    }

    @Override
    public TemplateResult build() throws TemplateEvaluationException {
        return new StringConverterTemplateResult(source, stringConverter);
    }
}

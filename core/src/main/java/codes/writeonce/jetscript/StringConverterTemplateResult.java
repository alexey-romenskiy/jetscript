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

import java.io.IOException;

import static codes.writeonce.jetscript.ValueType.EMPTY;
import static codes.writeonce.jetscript.ValueType.NOT_EMPTY;

class StringConverterTemplateResult extends AbstractTemplateResult {

    private final TemplateResult source;
    private final StringConverter stringConverter;

    public StringConverterTemplateResult(TemplateResult source, StringConverter stringConverter) {
        this.source = source;
        this.stringConverter = stringConverter;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        final String value = get();
        appendable.append(value);
        return !value.isEmpty();
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final String value = get();
        appendable.append(value);
        return value.isEmpty() ? EMPTY : NOT_EMPTY;
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final String value = get();
        stringBuilder.append(value);
        return !value.isEmpty();
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final String value = get();
        stringBuilder.append(value);
        return value.isEmpty() ? EMPTY : NOT_EMPTY;
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        return get();
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        return get();
    }

    private String get() throws TemplateEvaluationException {
        return stringConverter.convert(source.getStringValue());
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        // we must ensure that conversion is successful:
        get();
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        return !get().isEmpty();
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        return checkDefinedValueNotEmpty() ? NOT_EMPTY : EMPTY;
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        validate();
        return true;
    }
}

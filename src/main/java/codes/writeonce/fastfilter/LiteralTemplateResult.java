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

import java.io.IOException;

import static codes.writeonce.fastfilter.ValueType.NOT_EMPTY;

class LiteralTemplateResult extends AbstractTemplateResult {

    private final String value;

    public LiteralTemplateResult(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        appendable.append(value);
        return true;
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        appendable.append(value);
        return NOT_EMPTY;
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        stringBuilder.append(value);
        return true;
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        stringBuilder.append(value);
        return NOT_EMPTY;
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        return value;
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        return value;
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        // empty
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        return true;
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        return NOT_EMPTY;
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        return true;
    }
}

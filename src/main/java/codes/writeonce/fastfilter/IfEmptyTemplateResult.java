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

class IfEmptyTemplateResult extends AbstractTemplateResult {

    private final TemplateResult first;
    private final TemplateResult second;

    public IfEmptyTemplateResult(TemplateResult first, TemplateResult second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        return first.appendIfDefined(appendable) == NOT_EMPTY || second.append(appendable);
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final ValueType valueType = first.appendIfDefined(appendable);
        if (valueType == NOT_EMPTY) {
            return valueType;
        } else {
            return second.appendIfDefined(appendable);
        }
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        return first.appendIfDefined(stringBuilder) == NOT_EMPTY || second.append(stringBuilder);
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final ValueType valueType = first.appendIfDefined(stringBuilder);
        if (valueType == NOT_EMPTY) {
            return valueType;
        } else {
            return second.appendIfDefined(stringBuilder);
        }
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        final String value = first.getStringValueIfDefined();
        if (value == null || value.isEmpty()) {
            return second.getStringValue();
        } else {
            return value;
        }
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        final String value = first.getStringValueIfDefined();
        if (value == null || value.isEmpty()) {
            return second.getStringValueIfDefined();
        } else {
            return value;
        }
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        if (first.checkValueType() != NOT_EMPTY) {
            second.validate();
        }
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        return first.checkValueType() == NOT_EMPTY || second.checkDefinedValueNotEmpty();
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == NOT_EMPTY) {
            return valueType;
        } else {
            return second.checkValueType();
        }
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        return first.checkValueType() == NOT_EMPTY || second.isDefined();
    }
}

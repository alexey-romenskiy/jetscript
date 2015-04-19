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

class IfNotEmptyTemplateResult extends AbstractTemplateResult {

    private final TemplateResult first;
    private final TemplateResult second;

    public IfNotEmptyTemplateResult(TemplateResult first, TemplateResult second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        return first.checkDefinedValueNotEmpty() && second.append(appendable);
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == NOT_EMPTY) {
            return second.appendIfDefined(appendable);
        } else {
            return valueType;
        }
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        return first.checkDefinedValueNotEmpty() && second.append(stringBuilder);
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == NOT_EMPTY) {
            return second.appendIfDefined(stringBuilder);
        } else {
            return valueType;
        }
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        if (first.checkDefinedValueNotEmpty()) {
            return second.getStringValue();
        } else {
            return "";
        }
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        switch (valueType) {
            case NOT_EMPTY:
                return second.getStringValueIfDefined();
            case EMPTY:
                return "";
            case UNDEFINED:
                return null;
            default:
                throw new IllegalArgumentException("Unsupported value type: " + valueType);
        }
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        if (first.checkDefinedValueNotEmpty()) {
            second.validate();
        }
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        return first.checkDefinedValueNotEmpty() && second.checkDefinedValueNotEmpty();
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == NOT_EMPTY) {
            return second.checkValueType();
        } else {
            return valueType;
        }
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        switch (valueType) {
            case NOT_EMPTY:
                return second.isDefined();
            case EMPTY:
                return true;
            case UNDEFINED:
                return false;
            default:
                throw new IllegalArgumentException("Unsupported value type: " + valueType);
        }
    }
}

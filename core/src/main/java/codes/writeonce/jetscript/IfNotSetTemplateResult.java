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

import static codes.writeonce.jetscript.ValueType.UNDEFINED;

class IfNotSetTemplateResult extends AbstractTemplateResult {

    private final TemplateResult first;
    private final TemplateResult second;

    public IfNotSetTemplateResult(TemplateResult first, TemplateResult second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        final ValueType valueType = first.appendIfDefined(appendable);
        switch (valueType) {
            case UNDEFINED:
                return second.append(appendable);
            case EMPTY:
                return false;
            case NOT_EMPTY:
                return true;
            default:
                throw new IllegalArgumentException("Unsupported value type: " + valueType);
        }
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final ValueType valueType = first.appendIfDefined(appendable);
        if (valueType == UNDEFINED) {
            return second.appendIfDefined(appendable);
        } else {
            return valueType;
        }
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final ValueType valueType = first.appendIfDefined(stringBuilder);
        switch (valueType) {
            case UNDEFINED:
                return second.append(stringBuilder);
            case EMPTY:
                return false;
            case NOT_EMPTY:
                return true;
            default:
                throw new IllegalArgumentException("Unsupported value type: " + valueType);
        }
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final ValueType valueType = first.appendIfDefined(stringBuilder);
        if (valueType == UNDEFINED) {
            return second.appendIfDefined(stringBuilder);
        } else {
            return valueType;
        }
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        final String value = first.getStringValueIfDefined();
        if (value == null) {
            return second.getStringValue();
        } else {
            return value;
        }
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        final String value = first.getStringValueIfDefined();
        if (value == null) {
            return second.getStringValueIfDefined();
        } else {
            return value;
        }
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        if (!first.isDefined()) {
            second.validate();
        }
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        switch (valueType) {
            case UNDEFINED:
                return second.checkDefinedValueNotEmpty();
            case NOT_EMPTY:
                return true;
            case EMPTY:
                return false;
            default:
                throw new IllegalArgumentException("Unsupported value type: " + valueType);
        }
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == UNDEFINED) {
            return second.checkValueType();
        } else {
            return valueType;
        }
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        return first.isDefined() || second.isDefined();
    }
}

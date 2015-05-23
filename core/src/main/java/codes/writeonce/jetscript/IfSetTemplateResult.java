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

class IfSetTemplateResult extends AbstractTemplateResult {

    private final TemplateResult first;
    private final TemplateResult second;

    public IfSetTemplateResult(TemplateResult first, TemplateResult second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        first.validate();
        return second.append(appendable);
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == UNDEFINED) {
            return valueType;
        } else {
            return second.appendIfDefined(appendable);
        }
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        first.validate();
        return second.append(stringBuilder);
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == UNDEFINED) {
            return valueType;
        } else {
            return second.appendIfDefined(stringBuilder);
        }
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        first.validate();
        return second.getStringValue();
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        if (first.checkValueType() == UNDEFINED) {
            return null;
        } else {
            return second.getStringValueIfDefined();
        }
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        first.validate();
        second.validate();
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        first.validate();
        return second.checkDefinedValueNotEmpty();
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        final ValueType valueType = first.checkValueType();
        if (valueType == UNDEFINED) {
            return valueType;
        } else {
            return second.checkValueType();
        }
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        return first.isDefined() && second.isDefined();
    }
}

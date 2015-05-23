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

import static codes.writeonce.jetscript.ValueType.NOT_EMPTY;
import static codes.writeonce.jetscript.ValueType.UNDEFINED;

class PropertyValueLengthTemplateResult extends PropertyAccessorTemplateResult {

    public PropertyValueLengthTemplateResult(PropertyHolder propertyHolder, String propertyName,
            TextPosition position) {
        super(propertyHolder, propertyName, position);
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        appendable.append(String.valueOf(get().length()));
        return true;
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final String value = getPropertyValue();
        if (value == null) {
            return UNDEFINED;
        } else {
            appendable.append(String.valueOf(value.length()));
            return NOT_EMPTY;
        }
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        stringBuilder.append(get().length());
        return true;
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final String value = getPropertyValue();
        if (value == null) {
            return UNDEFINED;
        } else {
            stringBuilder.append(value.length());
            return NOT_EMPTY;
        }
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        return String.valueOf(get().length());
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        final String value = getPropertyValue();
        if (value == null) {
            return null;
        } else {
            return String.valueOf(value.length());
        }
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        get();
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        get();
        return true;
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        final String value = getPropertyValue();
        if (value == null) {
            return UNDEFINED;
        } else {
            return NOT_EMPTY;
        }
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        return getPropertyValue() != null;
    }
}

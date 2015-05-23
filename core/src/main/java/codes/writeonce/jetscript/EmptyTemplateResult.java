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

class EmptyTemplateResult extends AbstractTemplateResult {

    private static final EmptyTemplateResult INSTANCE = new EmptyTemplateResult();

    public static EmptyTemplateResult newInstance() {
        return INSTANCE;
    }

    private EmptyTemplateResult() {
        // empty
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        return false;
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        return EMPTY;
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        return false;
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        return EMPTY;
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        return "";
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        return "";
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        // empty
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        return false;
    }

    @Override
    public ValueType checkValueType() throws TemplateEvaluationException {
        return EMPTY;
    }

    @Override
    public boolean isDefined() throws TemplateEvaluationException {
        return true;
    }
}

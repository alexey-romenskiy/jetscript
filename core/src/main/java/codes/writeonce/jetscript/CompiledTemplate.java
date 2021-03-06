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

public class CompiledTemplate {

    private final TemplateResult result;

    CompiledTemplate(TemplateResult result) {
        this.result = result;
    }

    public boolean isConstant() {
        return result.isConstant();
    }

    public void evaluate(Appendable appendable) throws IOException, TemplateEvaluationException {
        result.append(appendable);
    }

    public void evaluate(StringBuilder stringBuilder) throws TemplateEvaluationException {
        result.append(stringBuilder);
    }

    public String evaluate() throws TemplateEvaluationException {
        return result.getStringValue();
    }

    public void validate() throws TemplateEvaluationException {
        result.validate();
    }
}

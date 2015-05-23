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

interface TemplateResult {

    boolean isConstant();

    /**
     * @throws TemplateEvaluationException if value not defined
     */
    boolean append(Appendable appendable) throws IOException, TemplateEvaluationException;

    ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException;

    /**
     * @throws TemplateEvaluationException if value not defined
     */
    boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException;

    ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException;

    /**
     * @throws TemplateEvaluationException if value not defined
     */
    String getStringValue() throws TemplateEvaluationException;

    String getStringValueIfDefined() throws TemplateEvaluationException;

    /**
     * @throws TemplateEvaluationException if value not defined
     */
    CharSequence getCharSequence() throws TemplateEvaluationException;

    CharSequence getCharSequenceIfDefined() throws TemplateEvaluationException;

    /**
     * @throws TemplateEvaluationException if value not defined
     */
    void validate() throws TemplateEvaluationException;

    /**
     * @return <code>true</code> if not empty
     * @throws TemplateEvaluationException if value not defined
     */
    boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException;

    ValueType checkValueType() throws TemplateEvaluationException;

    /**
     * @return <code>true</code> if defined
     */
    boolean isDefined() throws TemplateEvaluationException;
}

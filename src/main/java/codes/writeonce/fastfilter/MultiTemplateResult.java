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

import static codes.writeonce.fastfilter.ValueType.EMPTY;
import static codes.writeonce.fastfilter.ValueType.NOT_EMPTY;

class MultiTemplateResult extends AbstractTemplateResult {

    private final TemplateResult[] appenders;
    private final StringBuilder stringBuilder = new StringBuilder();

    public MultiTemplateResult(TemplateResult[] appenders) {
        this.appenders = appenders;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        boolean notEmpty = false;
        for (final TemplateResult appender : appenders) {
            notEmpty |= appender.append(appendable);
        }
        return notEmpty;
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        return append(appendable) ? NOT_EMPTY : EMPTY;
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        boolean notEmpty = false;
        for (final TemplateResult appender : appenders) {
            notEmpty |= appender.append(stringBuilder);
        }
        return notEmpty;
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        return append(stringBuilder) ? NOT_EMPTY : EMPTY;
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        return getCharSequence().toString();
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        return getStringValue();
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        for (final TemplateResult appender : appenders) {
            appender.validate();
        }
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        for (int i = 0; i < appenders.length; i++) {
            if (appenders[i].checkDefinedValueNotEmpty()) {
                for (i++; i < appenders.length; i++) {
                    appenders[i].validate();
                }
                return true;
            }
        }
        return false;
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

    @Override
    public CharSequence getCharSequence() throws TemplateEvaluationException {
        stringBuilder.setLength(0);
        for (final TemplateResult appender : appenders) {
            appender.append(stringBuilder);
        }
        return stringBuilder;
    }

    @Override
    public CharSequence getCharSequenceIfDefined() throws TemplateEvaluationException {
        return getCharSequence();
    }
}

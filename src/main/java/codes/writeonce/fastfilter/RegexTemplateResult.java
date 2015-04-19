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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static codes.writeonce.fastfilter.ValueType.EMPTY;
import static codes.writeonce.fastfilter.ValueType.NOT_EMPTY;

abstract class RegexTemplateResult extends AbstractTemplateResult {

    private final TextPosition position;
    protected final TemplateResult source;
    protected final Matcher matcher;
    protected final TemplateResult replacement;
    protected final StringBuffer stringBuffer = new StringBuffer();

    public RegexTemplateResult(TextPosition position, TemplateResult source, Pattern pattern,
            TemplateResult replacement) {
        this.position = position;
        this.source = source;
        this.matcher = pattern.matcher("");
        this.replacement = replacement;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean append(Appendable appendable) throws IOException, TemplateEvaluationException {
        final CharSequence value = get();
        appendable.append(value);
        return value.length() != 0;
    }

    @Override
    public ValueType appendIfDefined(Appendable appendable) throws IOException, TemplateEvaluationException {
        final CharSequence value = get();
        appendable.append(value);
        return value.length() == 0 ? EMPTY : NOT_EMPTY;
    }

    @Override
    public boolean append(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final CharSequence value = get();
        stringBuilder.append(value);
        return value.length() != 0;
    }

    @Override
    public ValueType appendIfDefined(StringBuilder stringBuilder) throws TemplateEvaluationException {
        final CharSequence value = get();
        stringBuilder.append(value);
        return value.length() == 0 ? EMPTY : NOT_EMPTY;
    }

    @Override
    public String getStringValue() throws TemplateEvaluationException {
        return get().toString();
    }

    @Override
    public String getStringValueIfDefined() throws TemplateEvaluationException {
        return getStringValue();
    }

    @Override
    public void validate() throws TemplateEvaluationException {
        get();
    }

    @Override
    public boolean checkDefinedValueNotEmpty() throws TemplateEvaluationException {
        return get().length() != 0;
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

    private CharSequence get() throws TemplateEvaluationException {
        try {
            return getUnsafe();
        } catch (TemplateEvaluationException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateEvaluationException(position, "Regular expression evaluation error", e);
        }
    }

    protected abstract CharSequence getUnsafe() throws TemplateEvaluationException;
}

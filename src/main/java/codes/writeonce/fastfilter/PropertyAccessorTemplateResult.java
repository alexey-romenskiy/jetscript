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

abstract class PropertyAccessorTemplateResult extends AbstractTemplateResult {

    private final PropertyHolder propertyHolder;
    protected final String propertyName;
    protected final TextPosition position;

    public PropertyAccessorTemplateResult(PropertyHolder propertyHolder, String propertyName, TextPosition position) {
        this.propertyHolder = propertyHolder;
        this.propertyName = propertyName;
        this.position = position;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public TextPosition getPosition() {
        return position;
    }

    protected String get() throws TemplateEvaluationException {
        final String value = getPropertyValue();
        if (value == null) {
            Errors.undefProperty(this);
        }
        return value;
    }

    protected String getPropertyValue() throws TemplateEvaluationException {
        try {
            return propertyHolder.getValue();
        } catch (PropertyResolverException e) {
            throw new TemplateEvaluationException(position, "Failed to get value of the property: " + propertyName, e);
        }
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}

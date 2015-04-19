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

class PropertyValueLengthTemplateResultBuilder extends SingleValueTemplateResultBuilder {

    private final PropertyHolder propertyHolder;
    private final String propertyName;
    private final TextPosition position;

    public static PropertyValueLengthTemplateResultBuilder newInstance(PropertyHolder propertyHolder,
            String propertyName, TextPosition position) {
        return new PropertyValueLengthTemplateResultBuilder(propertyHolder, propertyName, position);
    }

    private PropertyValueLengthTemplateResultBuilder(PropertyHolder propertyHolder, String propertyName,
            TextPosition position) {
        this.propertyHolder = propertyHolder;
        this.propertyName = propertyName;
        this.position = position;
    }

    @Override
    public TemplateResult build() throws TemplateEvaluationException {
        return new PropertyValueLengthTemplateResult(propertyHolder, propertyName, position);
    }
}

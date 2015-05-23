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

class EmptyTemplateResultBuilder extends AbstractEmptyTemplateResultBuilder {

    private static final EmptyTemplateResultBuilder INSTANCE = new EmptyTemplateResultBuilder();

    public static EmptyTemplateResultBuilder newInstance() {
        return INSTANCE;
    }

    private EmptyTemplateResultBuilder() {
        // empty
    }

    @Override
    public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
        return visitor.visit(this);
    }
}

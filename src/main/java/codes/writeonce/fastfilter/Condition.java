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

enum Condition {
    NOT_SET {
        @Override
        public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
            return visitor.visitNotSet();
        }
    },
    EMPTY {
        @Override
        public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
            return visitor.visitEmpty();
        }
    },
    SET {
        @Override
        public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
            return visitor.visitSet();
        }
    },
    NOT_EMPTY {
        @Override
        public <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E {
            return visitor.visitNotEmpty();
        }
    };

    public abstract <V, E extends Throwable> V accept(Visitor<V, E> visitor) throws E;

    public interface Visitor<V, E extends Throwable> {

        V visitNotSet() throws E;

        V visitEmpty() throws E;

        V visitSet() throws E;

        V visitNotEmpty() throws E;
    }
}

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

import java.io.Serializable;

public class TextPosition implements Serializable {

    private static final long serialVersionUID = -2248424770269380295L;

    public final int row;
    public final int column;

    public static TextPosition newPosition(CharSequence text, int position) {
        int row = 1;
        int column = 1;
        for (int i = 0; i < position; i++) {
            if (text.charAt(i) == '\n') {
                row++;
                column = 1;
            } else {
                column++;
            }
        }

        return new TextPosition(row, column);
    }

    public TextPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}

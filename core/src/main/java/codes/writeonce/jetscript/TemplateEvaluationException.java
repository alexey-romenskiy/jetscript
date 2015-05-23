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

public class TemplateEvaluationException extends Exception {

    private static final long serialVersionUID = -3479231829231425599L;

    private final TextPosition textPosition;
    private final String errorMessage;

    public TemplateEvaluationException(TextPosition textPosition, String errorMessage) {
        super(makeMessage(textPosition, errorMessage));
        this.textPosition = textPosition;
        this.errorMessage = errorMessage;
    }

    public TemplateEvaluationException(TextPosition textPosition, Throwable cause) {
        super(makeMessage(textPosition, cause.getMessage()), cause);
        this.textPosition = textPosition;
        this.errorMessage = cause.getMessage();
    }

    public TemplateEvaluationException(TextPosition textPosition, String errorMessage, Throwable cause) {
        super(makeMessage(textPosition, errorMessage), cause);
        this.textPosition = textPosition;
        this.errorMessage = errorMessage;
    }

    public TextPosition getTextPosition() {
        return textPosition;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private static String makeMessage(TextPosition textPosition, String errorMessage) {
        return "Error at " + textPosition + ": " + errorMessage;
    }
}

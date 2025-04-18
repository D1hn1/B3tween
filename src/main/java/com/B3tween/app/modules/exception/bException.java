package com.B3tween.app.modules.exception;

import com.B3tween.app.objects.enums.Exceptions;

/**
 * Extends Exception to create
 * custom error messages.
 */
public class bException extends Exception {
    public bException(Exceptions exception, String message) {
        super(exception.getLabel() + (message.isEmpty() ? "" : message));
    }   
}

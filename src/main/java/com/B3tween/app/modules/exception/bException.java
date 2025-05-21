package com.B3tween.app.modules.exception;

import com.B3tween.app.objects.enums.Exceptions;

public class bException extends Exception {
    public bException(Exceptions exception, String message) {
        super(exception.getLabel() + (message.isEmpty() ? "" : message));
    }   
}

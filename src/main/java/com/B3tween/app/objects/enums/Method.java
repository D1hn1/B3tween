package com.B3tween.app.objects.enums;

import lombok.Getter;
import com.B3tween.app.modules.exception.bException;

/**
 * Definition of a Method enum.
 * @return String label.
 */
@Getter
public enum Method {
    GET("get"),
    HEAD("head"),
    OPTIONS("options"),
    TRACE("trace"),
    PUT("put"),
    DELETE("delete"),
    POST("post"),
    PATCH("patch"),
    CONNECT("connect");

    private String label;

    Method(String label) {
        this.label = label;
    }

    /**
     * Gets the enum Method.
     * @param method String.
     * @return Method entity.
     * @throws bException If method is not found.
     */
    public static Method getMethod(String method) throws bException {
        for (Method thisMethod : Method.values()) {
            if (thisMethod.getLabel().toUpperCase().equals(method.toUpperCase())) {
                return thisMethod;
            }
        }
        throw new bException(Exceptions.METHOD_NOT_SUPPORTED, method);
    }
}

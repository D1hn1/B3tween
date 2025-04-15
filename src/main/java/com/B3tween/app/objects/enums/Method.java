package com.B3tween.app.objects.enums;

import lombok.Getter;

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
}

package com.B3tween.app.objects.enums;

import lombok.Getter; 

@Getter
public enum Exceptions {
    METHOD_NOT_IMPLEMENTED("Method not implemented"),
    HTTPS_NOT_IMPLEMENTED("Https is not yet implemented"),
    UNKNOWN_HOST("Host not found"),
    IO_CONN_ERR("IO Socket connection error"),
    SOCKET_CLOSE_ERROR("Socket close error"),
    MALFORMED_URL("Malformed URL"),
    METHOD_NOT_SUPPORTED("HTTP method not found or not supported"),
    NOT_AUTHORIZE("User not authorize");
    
    private String label;

    Exceptions(String label) {
        this.label = label;
    }
}

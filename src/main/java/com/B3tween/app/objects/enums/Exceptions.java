package com.B3tween.app.objects.enums;

import lombok.Getter; 

/**
 * Definition of Exceptions for
 * modules/log/bException.java.
 */
@Getter
public enum Exceptions {
    METHOD_NOT_IMPLEMENTED("Method not implemented"),
    HTTPS_NOT_IMPLEMENTED("Https is not yet implemented"),
    UNKNOWN_HOST("Host not found"),
    IO_CONN_ERR("IO Socket connection error"),
    SOCKET_CLOSE_ERROR("Socket close error"),
    MALFORMED_URL("Malformed URL");
    
    private String label;

    Exceptions(String label) {
        this.label = label;
    }
}

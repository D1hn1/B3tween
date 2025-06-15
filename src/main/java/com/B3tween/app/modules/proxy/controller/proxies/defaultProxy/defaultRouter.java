package com.B3tween.app.modules.proxy.controller.proxies.defaultProxy;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.proxy.controller.dto.connectionDto;
import com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.controller.defaultHttpHandler;
import com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.controller.defaultHttpsHandler;
import com.B3tween.app.objects.dto.requestDto;

public class defaultRouter {

    /**
     * Parsers HTTP and HTTPS connections
     * @param connectionData The connection DTO
     * @param user The user DTO
     */
    public static void methodParser(connectionDto connectionData, AuthDto user) {
        switch (connectionData.getRequest().getMethod()) {
            case CONNECT:
                defaultHttpsHandler.dispatchRequest(connectionData, user);
                break;
            default:
                defaultHttpHandler.dispatchRequest(connectionData, user);
                break;
        }
    }
}

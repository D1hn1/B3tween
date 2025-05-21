package com.B3tween.app.modules.proxy.connection.proxies.defaultProxy;

import com.B3tween.app.modules.proxy.connection.dto.connectionDto;
import com.B3tween.app.modules.proxy.connection.proxies.defaultProxy.defaultRouter;
import com.B3tween.app.modules.proxy.connection.proxies.defaultProxy.controller.defaultHttpHandler;
import com.B3tween.app.modules.proxy.connection.proxies.defaultProxy.controller.defaultHttpsHandler;
import com.B3tween.app.objects.dto.requestDto;

public class defaultRouter {

    public static void methodParser(connectionDto connectionData, requestDto request) {
        switch (request.getMethod()) {
            case CONNECT:
                defaultHttpsHandler.dispatchRequest(connectionData);
                break;
            default:
                defaultHttpHandler.dispatchRequest(connectionData);
                break;
        }
    }
}

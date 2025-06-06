package com.B3tween.app.modules.proxy.controller.proxies.defaultProxy;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.proxy.controller.dto.connectionDto;
import com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.defaultRouter;
import com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.controller.defaultHttpHandler;
import com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.controller.defaultHttpsHandler;
import com.B3tween.app.objects.dto.requestDto;

public class defaultRouter {

    public static void methodParser(connectionDto connectionData, requestDto request, AuthDto user) {
        switch (request.getMethod()) {
            case CONNECT:
                defaultHttpsHandler.dispatchRequest(connectionData, user);
                break;
            default:
                defaultHttpHandler.dispatchRequest(connectionData, user);
                break;
        }
    }
}

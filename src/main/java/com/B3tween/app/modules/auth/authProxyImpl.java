package com.B3tween.app.modules.auth;

import java.util.Base64;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;

public class authProxyImpl {
    
    public static Boolean validateLogin(requestDto request) {
        // check authentication enable
        if (globalRuntime.PROXY_AUTHENTICATION) {
            // parse headers
            for (headerDto header : request.getHeaders()) {

                // get Authorization header
                if (header.getKey().equals("Proxy-Authorization")) {
                    String authenticationType = header.getValue().split(" ")[0].trim();
                    String authenticationCreds = header.getValue().split(" ")[1].trim();
                    // parse Authorization types
                    if (authenticationType.equals("Basic")) {
                        byte[] credsDecBase64 = Base64.getDecoder().decode(authenticationCreds);
                        String proxyCredentials = new String(credsDecBase64);
                        String username = proxyCredentials.toString().split(":")[0].trim(); // TRIM THEM
                        String password = proxyCredentials.toString().split(":")[1].trim();
                        if (username.equals(globalRuntime.PROXY_USERNAME) && password.equals(globalRuntime.PROXY_PASSWORD)) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }
        return true;
    }

}

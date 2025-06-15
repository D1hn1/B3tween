package com.B3tween.app.modules.auth;

import java.util.Base64;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.auth.repository.authRepository;

public class authProxyImpl {

    /**
     * Validates an user in the proxy module
     * @param request The user requests
     * @return True if user exists False otherwhise
     */
    public static Boolean validateLogin(requestDto request) {
        // check authentication enable
        if (globalRuntime.PROXY_AUTHENTICATION) {
            // parse headers
            for (headerDto header : request.getHeaders()) {
                // get Authorization header
                if (header.getKey().equals("Proxy-Authorization")) {
                    String authenticationType = header.getValue().split(" ")[0].trim();
                    String authenticationCreds = header.getValue().split(" ")[1].trim();
                    // Parse Basic Auth
                    if (authenticationType.equalsIgnoreCase("Basic")) {
                        byte[] credsDecBase64 = Base64.getDecoder().decode(authenticationCreds);
                        String proxyCredentials = new String(credsDecBase64);
                        String username = proxyCredentials.toString().split(":")[0].trim(); // TRIM THEM
                        String password = proxyCredentials.toString().split(":")[1].trim();
                        if (authRepository.canUserLogin(username, password)) {
                            return true;
                        }
                    }
                    // Parse Bearer Auth
                    if (authenticationType.equalsIgnoreCase("Bearer")) {
                        if (authRepository.getUserByToken(authenticationCreds) != null) {
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

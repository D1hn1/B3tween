package com.B3tween.app.modules.proxy.token;

import java.util.Base64;
import java.security.SecureRandom;
import com.B3tween.app.objects.global.globalRuntime;

public class ProxyToken {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64encoder = Base64.getUrlEncoder().withoutPadding();

    /**
     * Generates a non colliding unique token
     * @return A unique token
     */
    public static String generate() {
        byte[] randomBytes =
                new byte[globalRuntime.proxyTokenLength];
        secureRandom.nextBytes(randomBytes);
        return base64encoder.encodeToString(randomBytes);
    }

}

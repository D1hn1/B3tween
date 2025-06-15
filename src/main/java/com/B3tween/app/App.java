package com.B3tween.app;

import java.io.IOException;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.www.api.apiListener;
import com.B3tween.app.modules.www.frontend.frontListener;
import com.B3tween.app.modules.proxy.proxyListener;
import com.B3tween.app.objects.global.globalRuntime;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            globalRuntime.threadPool.submit(() -> 
                new apiListener(globalRuntime.API_PORT));
            globalRuntime.threadPool.submit(() ->
                new frontListener(globalRuntime.WEB_PORT));
            new proxyListener(globalRuntime.PROXY_PORT);
        } catch (IOException ioe) {
            Log.c(ioe.getMessage());
        }
    }
}

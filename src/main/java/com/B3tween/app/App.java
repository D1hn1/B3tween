package com.B3tween.app;

import java.io.IOException;
import com.B3tween.app.modules.handler.listener.*;
import com.B3tween.app.modules.www.api.apiListener;
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
            new Listener(globalRuntime.PROXY_PORT);
        } catch (IOException ioe) {}
    }
}

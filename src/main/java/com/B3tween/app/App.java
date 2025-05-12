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
                new apiListener(8000));
            new Listener(8080);
        } catch (IOException ioe) {}
    }
}

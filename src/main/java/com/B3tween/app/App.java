package com.B3tween.app;

import java.io.IOException;
import com.B3tween.app.modules.handler.listener.*;
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
            new Listener(globalRuntime.PROXY_PORT);
        } catch (IOException ioe) {}
    }
}

package com.B3tween.app;

import java.io.IOException;

import com.B3tween.app.modules.handler.listener.*;
import com.B3tween.app.modules.exception.bException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            new Listener(8080);
        } catch (IOException ioe) {}
    }
}

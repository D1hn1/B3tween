package com.B3tween.app.modules.handler.listener;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.handler.handleClient.handler;
// ADD SHUTDOWN HOOK FOR CTRL CONTROL
public class Listener {
    
    public Listener(int port) throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);

        Log.i("Server listening on port: " + port);

        ExecutorService pool = Executors.newCachedThreadPool();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            pool.submit(() -> handler.handleClient(clientSocket));
        }
    }

}

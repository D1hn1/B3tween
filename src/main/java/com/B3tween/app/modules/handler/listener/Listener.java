package com.B3tween.app.modules.handler.listener;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.handler.handleConnection.handler;
import com.B3tween.app.modules.handler.utils.handlerUtils;

public class Listener {
    
    /**
     * Listener class
     * @param port Port to listen on
     * @throws IOException If an error occurs while closing the socket
     */
    public Listener(int port) throws IOException {

        // Initialize server socket
        ServerSocket serverSocket = new ServerSocket(port);
        Log.i("Server listening on port " + port);

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
            }
            catch (IOException io) {
                Log.e("Error while closing the socket " + io);
            };

            // Close all client connections & stop server
            handlerUtils.closeAllConnections();
            globalRuntime.RUNNING = false;
            Log.i("Ctrl-c pressed exiting");
        }));

        // Thread pool
        ExecutorService pool = Executors.newCachedThreadPool();
        while (globalRuntime.RUNNING) {

            // Accept clients
            Socket clientSocket = serverSocket.accept();
            pool.submit(() -> handler.Handler(clientSocket));

        }
    }

}

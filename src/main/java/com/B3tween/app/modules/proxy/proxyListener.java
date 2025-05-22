package com.B3tween.app.modules.proxy;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.controller.proxyController;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.objects.global.globalRuntime;

public class proxyListener {
    
    /**
     * Listener class
     * @param port Port to listen on
     * @throws IOException If an error occurs while closing the socket
     */
    public proxyListener(int port) throws IOException {

        // Initialize server socket
        ServerSocket serverSocket = new ServerSocket(port);
        Log.i("[PROXY] listener started on port " + port);

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
            }
            catch (IOException io) {
                Log.e("[HOOK] Error while closing the socket " + io);
            };

            // Close all client connections & stop server
            proxyUtils.closeAllConnections();
            globalRuntime.RUNNING = false;
            Log.i("[HOOK] Ctrl-c pressed exiting");
        }));

        while (globalRuntime.RUNNING) {

            // Accept clients
            Socket clientSocket = serverSocket.accept();
            globalRuntime.threadPool.submit(() -> proxyController.Handler(clientSocket));

        }
    }

}

package com.B3tween.app.modules.handler.listener;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.auth.authProxyImpl;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.handler.handleConnection.handler;
import com.B3tween.app.modules.handler.utils.handlerUtils;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.objects.enums.Exceptions;

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
            requestDto proxyRequest = handlerUtils.getRequest(clientSocket);

            // Validate proxy authentication
            if (globalRuntime.PROXY_AUTHENTICATION) {

                if (authProxyImpl.validateLogin(proxyRequest)) {
                    Log.i("Client: " + clientSocket.getRemoteSocketAddress() + " authenticated");
                    pool.submit(() -> handler.Handler(clientSocket, proxyRequest));
                } else {
                    handlerUtils.responses.proxyAuthenticationRequired(clientSocket);
                    Log.e("Client: " + clientSocket.getRemoteSocketAddress() + " failed auth");
                }

            } else {
                pool.submit(() -> handler.Handler(clientSocket, proxyRequest));
            }
        }
    }

}

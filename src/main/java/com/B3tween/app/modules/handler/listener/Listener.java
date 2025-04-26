package com.B3tween.app.modules.handler.listener;

import java.net.*;
import java.io.*;
import java.util.List;
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
    
    public static void proxyAuthenticationRequired(Socket clientSocket) {
        // HTTP/1.1 407 Proxy Authentication Required
        // Proxy-Authenticate: Basic realm="Proxy requires authentication"
        try {
            String data = "Bad credentials. Closing connection\n";
            BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            responseDto response = responseDto.response("HTTP/1.1", 407, 
                        "Proxy Authentication Required", 
                        List.of(headerDto.header("Proxy-Authenticate", "Basic realm=\"Proxy requires authentication\""),
                                headerDto.header("Content-Length", ""+data.length())), 
                        data);
            clientOut.write(response.toString());
            clientOut.flush();
        } catch (IOException io) {}
        
    }

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
                    Log.i("Client: " + clientSocket.getRemoteSocketAddress() + " success auth");
                    pool.submit(() -> handler.Handler(clientSocket, proxyRequest));
                } else {
                    proxyAuthenticationRequired(clientSocket);
                    Log.e("Client: " + clientSocket.getRemoteSocketAddress() + " failed auth");
                }

            } else {
                pool.submit(() -> handler.Handler(clientSocket, proxyRequest));
            }
        }
    }

}

package com.B3tween.app.modules.proxy.connection;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.connection.dto.connectionDto;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.modules.socket.initializeHttpSocket;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;

import java.io.*;
import java.net.*;
import com.B3tween.app.modules.exception.bException;

public class httpsProxyHandler {
    
    private static void relayBytes(InputStream in, OutputStream out, Socket clientSocket) {
        globalRuntime.threadPool.submit(() -> {
            try {
                while (!clientSocket.isClosed()) {
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                    if (bytesRead == -1) break;
                }
                clientSocket.close();
            } catch (IOException io) {}
        });
    }

    public static void dispatchRequest(connectionDto connectionData) {

        try {
            // Initialize IO and Sockets
            initializeHttpSocket forwardSocket = 
                new initializeHttpSocket(connectionData.getRequest(),
                ""+connectionData.getClientSocket().getRemoteSocketAddress());

            InputStream serverIn = forwardSocket.socket.getInputStream();
            OutputStream serverOutBytes = forwardSocket.socket.getOutputStream();
            InputStream clientIn = connectionData.getClientSocket().getInputStream();
            OutputStream clientOutBytes = connectionData.getClientSocket().getOutputStream();

            // Client socket timeout
            connectionData.getClientSocket().setSoTimeout(1000);

            requestDto request = connectionData.getRequest();
            if (request.equals(null)) {
                return;
            }

            // Send connection response
            proxyUtils.responses.connectionEstablished(connectionData.getClientSocket());

            // Transmit data
            relayBytes(clientIn, serverOutBytes, connectionData.getClientSocket());
            relayBytes(serverIn, clientOutBytes, connectionData.getClientSocket());

            if (!connectionData.isKeepAlive()) return;

        } catch (IOException io)
        {} catch (bException e) {
            Log.e("Error: " + e);
        }

    }

}
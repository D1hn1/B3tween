package com.B3tween.app.modules.proxy.connection;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.connection.dto.connectionDto;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.modules.socket.initializeSocket;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;

import java.io.*;
import java.net.*;
import com.B3tween.app.modules.exception.bException;

public class proxyHttpsHandler {
    
    /**
     * Relay bytes between client and server.
     * @param in Read Socket 
     * @param out Write Socket
     * @param clientSocket Client Socket
     */
    private static void relayBytes(InputStream in, OutputStream out, Socket clientSocket) {
        globalRuntime.threadPool.submit(() -> {
            try {
                // Recv data & send it
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                // Close socket
                clientSocket.close();
            } catch (IOException io) {}
        });
    }

    /**
     * Handle the proxy https connection.
     * @param connectionData The connection DTO.
     */
    public static void dispatchRequest(connectionDto connectionData) {

        try {
            // Initialize IO and Sockets
            initializeSocket forwardSocket = 
                new initializeSocket(connectionData.getRequest(),
                ""+connectionData.getClientSocket().getRemoteSocketAddress());

            InputStream serverIn = forwardSocket.socket.getInputStream();
            OutputStream serverOutBytes = forwardSocket.socket.getOutputStream();
            InputStream clientIn = connectionData.getClientSocket().getInputStream();
            OutputStream clientOutBytes = connectionData.getClientSocket().getOutputStream();

            // Client socket timeout
            connectionData.getClientSocket().setSoTimeout(5000);

            requestDto request = connectionData.getRequest();
            if (request == null) {
                return;
            }

            // Send connection response
            proxyUtils.responses.connectionEstablished(connectionData.getClientSocket());

            // Transmit data
            relayBytes(clientIn, serverOutBytes, connectionData.getClientSocket());
            relayBytes(serverIn, clientOutBytes, connectionData.getClientSocket());

        } catch (IOException io)
        {} catch (bException e) {
            Log.e("[PROXY] Error: " + e);
        }

    }

}
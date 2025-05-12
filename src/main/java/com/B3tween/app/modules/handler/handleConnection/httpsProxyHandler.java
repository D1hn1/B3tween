package com.B3tween.app.modules.handler.handleConnection;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.socket.initializeHttpSocket;
import com.B3tween.app.objects.dto.requestDto;

import java.io.*;
import java.net.*;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.handler.handleConnection.dto.connectionDto;
import com.B3tween.app.modules.handler.utils.handlerUtils;

public class httpsProxyHandler {
    
    private static void relayBytes(InputStream in, OutputStream out, Socket clientSocket) {
        new Thread(() -> {
            try {
                while (!clientSocket.isClosed()) {
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                }
            } catch (IOException io) {}
        }).start();
    }

    public static void dispatchRequest(connectionDto connectionData) {

        try {
            // Initialize IO and Sockets
            initializeHttpSocket forwardSocket = 
                new initializeHttpSocket(connectionData.getRequest(),
                ""+connectionData.getClientSocket().getRemoteSocketAddress());

            BufferedWriter serverOut = forwardSocket.out;
            InputStream serverIn = forwardSocket.socket.getInputStream();
            OutputStream serverOutBytes = forwardSocket.socket.getOutputStream();
            InputStream clientIn = connectionData.getClientSocket().getInputStream();
            OutputStream clientOutBytes = connectionData.getClientSocket().getOutputStream();

            // Client socket timeout
            connectionData.getClientSocket().setSoTimeout(1000);

            // while (!connectionData.getClientSocket().isClosed()) { // MAKES CPU SPIKE UP

            requestDto request = connectionData.getRequest();
            if (request.equals(null)) {
                return;
            }

            // Delete Proxy-Authorization & Proxy-Connection from request
            request.getHeaders().removeIf(header -> header.getKey().toLowerCase().equals("proxy-authorization"));
            request.getHeaders().removeIf(header -> header.getKey().toLowerCase().equals("proxy-connection"));
            if (connectionData.isKeepAlive()) {
                request.getHeaders().add(headerDto.header("Connection", "Keep-Alive"));
            }

            // Send request
            //serverOutBytes.write(request.toString().getBytes());
            //serverOutBytes.flush();

            // Send connection response
            handlerUtils.responses.connectionEstablished(connectionData.getClientSocket());

            // Transmit data
            relayBytes(clientIn, serverOutBytes, connectionData.getClientSocket());
            relayBytes(serverIn, clientOutBytes, connectionData.getClientSocket());

            if (!connectionData.isKeepAlive()) return;

            //}

            connectionData.getClientSocket().close();

        } catch (IOException io)
        {} catch (bException e) {
            Log.e("Error: " + e);
        }

    }

}

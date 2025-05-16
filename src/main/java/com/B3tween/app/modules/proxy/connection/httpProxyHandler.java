package com.B3tween.app.modules.proxy.connection;

import java.io.*;
import java.net.*;
import java.io.IOException;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.connection.dto.connectionDto;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.socket.initializeHttpSocket;

public class httpProxyHandler {
    
    private static void closeSockets(connectionDto connectionData, Socket serverSocket) throws IOException {
        connectionData.getClientSocket().close();
        serverSocket.close();
        globalRuntime.connectionList.remove(connectionData);
    }

    public static void dispatchRequest(connectionDto connectionData) {

        try {
            // Initialize IO and Sockets 
            initializeHttpSocket forwardSocket =
                new initializeHttpSocket(connectionData.getRequest(),
                ""+connectionData.getClientSocket().getRemoteSocketAddress());

            BufferedWriter serverOut = forwardSocket.out;
            InputStream serverIn = forwardSocket.socket.getInputStream();
            OutputStream clientOutBytes = connectionData.getClientSocket().getOutputStream();

            // Client socket timeout
            connectionData.getClientSocket().setSoTimeout(1000);

            while (!connectionData.getClientSocket().isClosed()) {
                // Get client request
                requestDto request = connectionData.getRequest();
                if (request.equals(null)) {
                    break;
                }

                // Delete Proxy-Authorization & Proxy-Connection from request
                request.getHeaders().removeIf(header -> header.getKey().toLowerCase().equals("proxy-authorization"));
                request.getHeaders().removeIf(header -> header.getKey().toLowerCase().equals("proxy-connection"));
                if (connectionData.isKeepAlive()) {
                    request.getHeaders().add(headerDto.header("Connection", "Keep-Alive"));
                }

                // Send request
                serverOut.write(request.toString());
                serverOut.flush();

                // Recv data 
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = serverIn.read(buffer)) != -1) {
                    clientOutBytes.write(buffer, 0, bytesRead);
                }
                clientOutBytes.flush();

                if (!connectionData.isKeepAlive()) break;

            }

            // Close sockets
            closeSockets(connectionData, forwardSocket.socket);

        } catch (IOException io)
        {} catch (bException e) {
            proxyUtils.responses.proxyBadGateway(
                    connectionData.getClientSocket());
            Log.e("Error: " + e);
        }

    }

}

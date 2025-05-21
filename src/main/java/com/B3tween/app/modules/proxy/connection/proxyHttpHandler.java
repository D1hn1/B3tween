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
import com.B3tween.app.modules.socket.initializeSocket;

public class proxyHttpHandler {
    
    /**
     * Closes all the connection sockets.
     * @param connectionData The connection DTO.
     * @param serverSocket The forward socket.
     * @throws IOException If an error occurs while closing the socket.
     */
    private static void closeSockets(connectionDto connectionData, Socket serverSocket) throws IOException {
        globalRuntime.connectionList.remove(connectionData);
        connectionData.getClientSocket().close();
        serverSocket.close();
    }

    /**
     * Handles the HTTP connections.
     * @param connectionData The connection DTO.
     */
    public static void dispatchRequest(connectionDto connectionData) {

        try {
            // Initialize IO and Sockets 
            initializeSocket forwardSocket =
                new initializeSocket(connectionData.getRequest(),
                ""+connectionData.getClientSocket().getRemoteSocketAddress());

            BufferedWriter serverOut = forwardSocket.out;
            BufferedReader serverIn = forwardSocket.in;
            BufferedWriter clientOut = connectionData.getClientOut();

            // Client socket timeout
            connectionData.getClientSocket().setSoTimeout(5000);

            while (!connectionData.getClientSocket().isClosed()) {
                // Get client request
                requestDto request = connectionData.getRequest();
                if (request == null)
                    break;

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
                String line = "";
                int contentLength = 0;
                boolean isContentLength = false;
                StringBuilder response = new StringBuilder();
                while ((line = serverIn.readLine()) != null && !line.isEmpty()) {
                    // Check for content length
                    if (line.toLowerCase().startsWith("content-length")) {
                        isContentLength = true;
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                    // Check for Connection close
                    if (line.toLowerCase().startsWith("connection")) {
                        if (line.split(":")[1].trim().toLowerCase().startsWith("close")) {
                            connectionData.setKeepAlive(false);
                        }
                    }
                    // Append to response
                    response.append(line).append("\r\n");
                }

                if (isContentLength) {
                    // Get body response
                    char[] rawBody = new char[contentLength];
                    serverIn.read(rawBody);
                    // Append to response
                    response.append(new String(rawBody));
                }

                // Send data
                clientOut.write(response.toString());
                clientOut.flush();

                if (!connectionData.isKeepAlive())
                    break;

            }

            // Close sockets
            closeSockets(connectionData, forwardSocket.socket);

        } catch (IOException io)
        {} catch (bException e) {
            proxyUtils.responses.proxyBadGateway(
                    connectionData.getClientSocket());
            Log.e("[PROXY] Error: " + e);
        }

    }

}

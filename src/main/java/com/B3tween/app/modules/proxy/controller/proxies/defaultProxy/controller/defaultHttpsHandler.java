package com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.controller;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.controller.dto.connectionDto;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.modules.socket.initializeSocket;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;

import java.io.*;
import java.net.*;
import java.time.Instant;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.exception.bException;

public class defaultHttpsHandler {
    
    /**
     * Deletes a connection by the user ID
     * @param userId The user ID
     */
    private static void deleteConnectionDto(int userId) {
        globalRuntime.connectionList.removeIf(conn -> 
            conn.getUserId() == userId
        );
    }

    /**
     * Relay bytes between client and server.
     * @param in Read Socket 
     * @param out Write Socket
     * @param clientSocket Client Socket
     */
    private static void relayBytesClientServer(InputStream in, OutputStream out, Socket clientSocket, AuthDto user) {
        globalRuntime.threadPool.submit(() -> {
            try {
                // Recv data & send it
                int bytesRead, totalData = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = in.read(buffer)) != -1) {
                    totalData+=bytesRead;
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                // Close socket
                clientSocket.close();
                // Delete connection DTO
                deleteConnectionDto(user.getId());
                // Update user
                user.setTx(user.getTx()+totalData);
                user.setUpdatedAt(Instant.now().toEpochMilli());
            } catch (IOException io) {}
        });
    }

    /**
     * Relay bytes between server and client.
     * @param in Read Socket
     * @param out Write Socket
     * @param clientSocket Client Socket
     */
    private static void relayBytesServerClient(InputStream in, OutputStream out, Socket clientSocket, AuthDto user) {
        globalRuntime.threadPool.submit(() -> {
            try {
                // Recv data & send it
                int bytesRead, totalData = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = in.read(buffer)) != -1) {
                    totalData+=bytesRead;
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                // Close socket
                clientSocket.close();
                // Delete connection DTO
                deleteConnectionDto(user.getId());
                // Update user
                user.setRx(user.getRx()+totalData);
                user.setUpdatedAt(Instant.now().toEpochMilli());
            } catch (IOException io) {}
        });
    }
    /**
     * Handle the proxy https connection.
     * @param connectionData The connection DTO.
     */
    public static void dispatchRequest(connectionDto connectionData, AuthDto user) {

        try {
            // Initialize IO and Sockets
            initializeSocket forwardSocket = 
                new initializeSocket(connectionData.getRequest(),
                ""+connectionData.getClientSocket().getRemoteSocketAddress());

            InputStream serverIn = forwardSocket.socket.getInputStream();
            OutputStream serverOut = forwardSocket.socket.getOutputStream();
            InputStream clientIn = connectionData.getClientSocket().getInputStream();
            OutputStream clientOut = connectionData.getClientSocket().getOutputStream();

            requestDto request = connectionData.getRequest();
            if (request == null) {
                return;
            }

            // Send connection response
            proxyUtils.responses.connectionEstablished(connectionData.getClientSocket());

            // Transmit data
            relayBytesClientServer(clientIn, serverOut, connectionData.getClientSocket(), user);
            relayBytesServerClient(serverIn, clientOut, connectionData.getClientSocket(), user);

        } catch (IOException io)
        {} catch (bException e) {
            Log.e("[PROXY] Error: " + e);
        }

    }

}
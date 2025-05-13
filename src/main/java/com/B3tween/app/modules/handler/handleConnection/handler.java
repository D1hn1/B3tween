package com.B3tween.app.modules.handler.handleConnection;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.authProxyImpl;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.handler.utils.handlerUtils;
import com.B3tween.app.modules.handler.handleConnection.dto.connectionDto;

public class handler {
 
    public static void Handler(Socket clientSocket) {

        // Get client request
        requestDto request = handlerUtils.getRequest(clientSocket);

        // Validate proxy authentication
        if (globalRuntime.PROXY_AUTHENTICATION) {
            if (!authProxyImpl.validateLogin(request)) {
                handlerUtils.responses.proxyAuthenticationRequired(clientSocket);
                Log.e("Client: " + clientSocket.getRemoteSocketAddress() + " failed authentication");
                return;
            }
            //Log.i("Client: " + clientSocket.getRemoteSocketAddress() + " authenticated");
        }

        // Get user I/O
        BufferedReader clientIn = null;
        BufferedWriter clientOut = null;
        try {
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException io) {}

        // Get Keep-Alive header
        boolean isKeepAlive = false;
        for (headerDto header : request.getHeaders()) {
            if (header.getKey().equals("Proxy-Connection")) {
                if (header.getValue().trim().toLowerCase().equals("keep-alive")) {
                    isKeepAlive = true;
                }
            }
        }

        // Connection DTO
        connectionDto connectionData = connectionDto.builder()
            .id(handlerUtils.getNextId())
            .request(request)
            .clientSocket(clientSocket)
            .clientIn(clientIn)
            .clientOut(clientOut)
            .isKeepAlive(isKeepAlive)
            .bytesIn(request.getData().length())
            .build();
        globalRuntime.connectionList.add(connectionData);

        // ADD HTTP/HTTPS SUPPORT
        switch (request.getMethod()) {
            case CONNECT:
                // ADD FOR JUST NOW THE FORWARD BYTE APROACH
                // USE THE INITIALIZESOCKET CLASS AND CREATE ANOTHER FOR TLS 
                httpsProxyHandler.dispatchRequest(connectionData);
                break;
        
            default:
                // ADD FOR JUST NOW THE FORWARD DATA
                // USE THE INITIALIZESOCKET CLASS
                httpProxyHandler.dispatchRequest(connectionData);
                break;
        }

    }

}

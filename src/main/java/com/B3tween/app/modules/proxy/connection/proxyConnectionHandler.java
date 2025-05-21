package com.B3tween.app.modules.proxy.connection;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.connection.dto.connectionDto;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.authProxyImpl;
import com.B3tween.app.objects.global.globalRuntime;

public class proxyConnectionHandler {
 
    public static void Handler(Socket clientSocket) {

        // Get client request
        requestDto request = proxyUtils.getRequest(clientSocket);

        // Validate proxy authentication
        if (globalRuntime.PROXY_AUTHENTICATION) {
            if (!authProxyImpl.validateLogin(request)) {
                proxyUtils.responses.proxyAuthenticationRequired(clientSocket);
                Log.e("[PROXY] Client: " + clientSocket.getRemoteSocketAddress() + " failed authentication");
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
            .id(proxyUtils.getNextId())
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
                proxyHttpsHandler.dispatchRequest(connectionData);
                break;
        
            default:
                proxyHttpHandler.dispatchRequest(connectionData);
                break;
        }

    }

}

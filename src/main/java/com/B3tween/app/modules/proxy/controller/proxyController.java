package com.B3tween.app.modules.proxy.controller;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.controller.dto.connectionDto;
import com.B3tween.app.modules.proxy.controller.proxies.defaultProxy.defaultRouter;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.authProxyImpl;
import com.B3tween.app.objects.global.globalRuntime;

public class proxyController {
 
    /**
     * Handles the client connection.
     * @param clientSocket The client socket.
     */
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

        // Parse ProxyType 
        switch (globalRuntime.proxyType) {
            case FORWARD:
                defaultRouter.methodParser(connectionData, request);
                break;
        
            default:
                break;
        }

    }

}

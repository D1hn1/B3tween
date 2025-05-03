package com.B3tween.app.modules.handler.handleConnection;

import java.io.*;
import java.net.*;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.handler.utils.handlerUtils;
import com.B3tween.app.modules.handler.handleConnection.dto.connectionDto;
import com.B3tween.app.modules.log.Log;

public class handler {
 
    public static void Handler(Socket clientSocket, requestDto request) {

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
                if (header.getValue().trim().equals("Keep-Alive")) {
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

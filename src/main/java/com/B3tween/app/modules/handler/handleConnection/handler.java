package com.B3tween.app.modules.handler.handleConnection;

import java.io.*;
import java.net.*;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.handler.utils.handlerUtils;
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

        // Get keep-alive headers
        boolean isKeepAlive = false;
        for (headerDto header : request.getHeaders()) {
            if (header.getKey().equals("Proxy-Connection")) {
                if (header.getValue().trim().equals("Keep-Alive")) {
                    isKeepAlive = true;
                }
            }
        }

        // ADD HTTP/HTTPS SUPPORT
        switch (request.getMethod()) {
            case CONNECT:
                // ADD FOR JUST NOW THE FORWARD BYTE APROACH
                // USE THE INITIALIZESOCKET CLASS AND CREATE ANOTHER FOR TLS 
                break;
        
            default:
                // ADD FOR JUST NOW THE FORWARD DATA
                // USE THE INITIALIZESOCKET CLASS
                break;
        }

    }

}

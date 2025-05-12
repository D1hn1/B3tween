package com.B3tween.app.modules.www.api.routes;

import java.io.*;
import java.net.*;
import java.util.List;

import com.B3tween.app.modules.handler.utils.handlerUtils;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;

public class handleRoutes {
    
    public static void Handle(Socket clientSocket) {

        try (BufferedWriter writer = 
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            // Get request from client
            requestDto request = handlerUtils.getRequest(clientSocket);
            Log.i("[API] User requested " + request.getURL().getPath());

            // Parse routes
            switch (request.getURL().getPath()) {
                case "/":
                    writer.write("HTTP/1.1 200 OK\r\n\r\n");
                    writer.flush();
                    break;
            
                default:
                    apiUtils.responses.resourceNotFound(clientSocket);
                    break;
            }

            clientSocket.close();

        } catch (IOException io) {}

    }
    
}

package com.B3tween.app.modules.www.api.routes;

import java.io.*;
import java.net.*;
import java.util.List;

import com.B3tween.app.modules.handler.utils.handlerUtils;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;

public class handleRoutes {
    
    public static void Handle(Socket clientSocket) {

        try (BufferedWriter writer = 
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            // Get request from client
            requestDto request = handlerUtils.getRequest(clientSocket);

            // Parse routes
            switch (request.getURL().getPath()) {
                case "/":
                    String data = "Response ok";
                    responseDto response = responseDto.builder()
                        .httpVersion("HTTP/1.1")
                        .statusCode(200)
                        .reasonPhrase("OK")
                        .headers(List.of(headerDto.header("Content-Length", ""+data.length())))
                        .data(data)
                        .build();
                    writer.write(response.toString());
                    writer.flush();
                    break;
            
                default:
                    break;
            }

            clientSocket.close();

        } catch (IOException io) {}

    }
    
}

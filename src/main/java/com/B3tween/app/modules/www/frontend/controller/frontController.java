package com.B3tween.app.modules.www.frontend.controller;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.www.frontend.utils.webUtils;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.enums.Method;

public class frontController {
   
    public static void Handle(Socket clientSocket) {

        try {
             
            // Get I/O from client Socket
            BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Get request from client
            requestDto request = proxyUtils.getRequest(clientSocket);
            Log.i("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                + request.getURL().getPath());

            // Get file from path
            String file = request.getURL().getPath().replace('/', ' ').trim();

            switch (request.getURL().getPath()) {
                case "/":
                    if (request.getMethod().equals(Method.GET)) {
                        // Send response
                        webUtils.responseFound(writer, "index.html");
                    } else {
                        apiUtils.responses.methodNotAllowed(clientSocket);
                    }
                    break;
            
                default:
                    if (request.getMethod().equals(Method.GET)) {
                        // Get file content
                        String requestFile = webUtils.readFile(file);
                        // Check for not existing files 
                        if (requestFile == null) {
                            String notFoundFile = webUtils.readFile("404.html");
                            writer.write(notFoundFile);
                            writer.flush();
                            return;
                        }
                        // Send file
                        writer.write(requestFile);
                        writer.flush();
                    } else {
                        apiUtils.responses.methodNotAllowed(clientSocket);
                    }
                    break;
            }

            clientSocket.close();
        } catch (IOException io) {}

    }   

}

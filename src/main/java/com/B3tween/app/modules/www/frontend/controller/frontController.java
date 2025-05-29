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

            // Get file from path
            String file = request.getURL().getPath();

            switch (request.getURL().getPath()) {
                case "/":
                    // Filter methods {@GET,.}
                    if (request.getMethod().equals(Method.GET)) {
                        // Send response
                        webUtils.responseFound(writer, "/index.html");
                        Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                            + request.getURL().getPath());
                    } else {
                        apiUtils.responses.methodNotAllowed(clientSocket);
                    }
                    break;
            
                default:
                    // Filter methods {@GET,.}
                    if (request.getMethod().equals(Method.GET)) {
                        // Check file existance
                        if (webUtils.doFileExists(file)) {
                            // Send file
                            webUtils.responseFound(writer, file);
                            Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                                + request.getURL().getPath());
                        } else {
                            // Not found
                            webUtils.responseNotFound(writer, "/404.html");
                            // Log
                            Log.e("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                                + request.getURL().getPath());
                        }
                    } else {
                        apiUtils.responses.methodNotAllowed(clientSocket);
                    }
                    break;
            }

            clientSocket.close();
        } catch (IOException io) {}

    }   

}

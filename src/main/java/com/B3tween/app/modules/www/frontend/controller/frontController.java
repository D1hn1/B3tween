package com.B3tween.app.modules.www.frontend.controller;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.modules.www.frontend.utils.webUtils;
import com.B3tween.app.modules.www.jwt.repository.jwtRepository;
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

            // Parse routes
            switch (request.getURL().getPath()) {

                case "/":
                    // Parse methods (eg., GET, POST)
                    if (request.getMethod().equals(Method.GET)) {
                        if (jwtRepository.validateJWT(request)) {
                            // User already logged in
                            webUtils.responseFound(writer, "/dashboard.html");
                        } else {
                            // Send response
                            webUtils.responseFound(writer, "/index.html");
                        }
                        Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                            + request.getURL().getPath());
                    // Other methods
                    } else {
                        // Send method not allowed
                        webUtils.methodNotAllowed(writer);
                    }
                    break;
 
                case "/login":
                    // Parse methods (eg., GET, POST)
                    if (request.getMethod().equals(Method.GET)) {
                        if (jwtRepository.validateJWT(request)) {
                            // User already logged in
                            webUtils.responseFound(writer, "/dashboard.html");
                        } else {
                            // Send response
                            webUtils.responseFound(writer, "/index.html");
                        }
                        Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                            + request.getURL().getPath());
                    // Other methods
                    } else {
                        // Send method not allowed
                        webUtils.methodNotAllowed(writer);
                    }
                    break;

                case "/register":
                    if (request.getMethod().equals(Method.GET)) {
                        // Send response
                        webUtils.responseFound(writer, "/register.html");
                        Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                            + request.getURL().getPath());
                    // Other methods
                    } else {
                        // Send method not allowed
                        webUtils.methodNotAllowed(writer);
                    }
                    break;
                
                case "/dashboard":
                    if (request.getMethod().equals(Method.GET)) {
                        // Validate JWT
                        if (jwtRepository.validateJWT(request)) {
                            webUtils.responseFound(writer, "/dashboard.html");
                            Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                                + request.getURL().getPath());
                        } else {
                            // JWT Not valid
                            webUtils.forbiddenAuth(writer, "/403.html");
                        }
                    } else {
                        // Send method not allowed
                        webUtils.methodNotAllowed(writer);
                    }
                    break;

                default:
                    // Resources
                    String filePath = request.getURL().getPath().trim();
                    // Check for folder retrieval
                    if (filePath.contains("/css/") ||
                        filePath.contains("/js/")) {
                        // Check file existance
                        if (webUtils.doFileExists(filePath)) {
                            // Send response
                            webUtils.responseFound(writer, filePath);
                            Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                                + request.getURL().getPath());
                            break;
                        }
                    } else if (filePath.contains(".ico")) {
                        // Send favicon
                        webUtils.sendFavicon(clientSocket, "/favicon.ico");
                        Log.l("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                            + request.getURL().getPath());
                        break;
                    }
                    // Send a 404 when a resource is not found
                    webUtils.responseNotFound(writer, "/404.html");
                    Log.e("[WEB] " + request.getMethod().getLabel().toUpperCase() + " "
                        + request.getURL().getPath());
                    break;
            }

            clientSocket.close();
        } catch (IOException io) {}

    }   

}

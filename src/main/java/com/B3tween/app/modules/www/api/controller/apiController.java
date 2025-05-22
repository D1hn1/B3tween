package com.B3tween.app.modules.www.api.controller;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.www.api.controller.routes.apiLogin;
import com.B3tween.app.modules.www.api.controller.routes.apiLogout;
import com.B3tween.app.modules.www.api.controller.routes.apiRegister;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.proxy.utils.proxyUtils;

public class apiController {
    
    public static void Handle(Socket clientSocket) {

        try (BufferedWriter writer = 
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            // Get request from client
            requestDto request = proxyUtils.getRequest(clientSocket);
            Log.i("[API] " + request.getMethod().getLabel().toUpperCase() + " " 
                + request.getURL().getPath());

            // Parse routes
            switch (request.getURL().getPath()) {

                /**
                 *  Root: (/)
                 *  Functions like a Health Check
                 */
                case "/":
                    apiUtils.responses.twoHundredOk(clientSocket);
                    break;
                
                /**
                 *  POST /auth/register
                 * 
                 *  Expected JSON body:
                 *  - username: String
                 *  - password: String
                 */
                case "/auth/register":
                    apiRegister.h(request, clientSocket);
                    break;
 
                /**
                 *  POST /auth/login
                 * 
                 *  Expected JSON body
                 *  - usename: String
                 *  - password: String
                 */
                case "/auth/login":
                    apiLogin.h(request, clientSocket);
                    break;
                
                /**
                 *  /logout
                 */
                case "/logout":
                    apiLogout.h(request, clientSocket);
                    break;

                default:
                    apiUtils.responses.resourceNotFound(clientSocket);
                    break;
            }

            clientSocket.close();

        } catch (IOException io) {}

    }
    
}

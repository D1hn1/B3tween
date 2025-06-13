package com.B3tween.app.modules.www.api.controller;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.www.api.controller.routes.apiGetConns;
import com.B3tween.app.modules.www.api.controller.routes.apiGetRxTx;
import com.B3tween.app.modules.www.api.controller.routes.apiGetToken;
import com.B3tween.app.modules.www.api.controller.routes.apiGetTotalConns;
import com.B3tween.app.modules.www.api.controller.routes.apiGetTotalUsers;
import com.B3tween.app.modules.www.api.controller.routes.apiLogin;
import com.B3tween.app.modules.www.api.controller.routes.apiRegister;
import com.B3tween.app.modules.www.api.controller.routes.apiSetProxyAuth;
import com.B3tween.app.modules.www.api.controller.routes.apiSetProxyType;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.proxy.utils.proxyUtils;

public class apiController {
    
    public static void Handle(Socket clientSocket) {

        try {

            // Get request from client
            requestDto request = proxyUtils.getRequest(clientSocket);
            Log.i("[API] " + request.getMethod().getLabel().toUpperCase() + " " 
                + request.getURL().getPath());

            // Get origin
            String origin = apiUtils.getOrigin(request);

            // Parse routes
            switch (request.getURL().getPath()) {

                /**
                 *  Root: (/)
                 *  Functions like a Health Check
                 */
                case "/":
                    apiUtils.responses.twoHundredOk(clientSocket, origin);
                    break;
                
                /**
                 *  POST /auth/register
                 *  Expected JSON body:
                 *  - username: String
                 *  - password: String
                 */
                case "/auth/register":
                    apiRegister.h(request, clientSocket, origin);
                    break;
 
                /**
                 *  POST /auth/login
                 *  Expected JSON body
                 *  - username: String
                 *  - password: String
                 */
                case "/auth/login":
                    apiLogin.h(request, clientSocket, origin);
                    break;

                /**
                 *  POST /get-token
                 *  Expected JSON body
                 *  - uid: String (UserId)
                 */
                case "/get-token":
                    apiGetToken.h(request, clientSocket, origin);
                    break;

                /**
                 *  POST /get-rxtx
                 *  Expected JSON body
                 *  - uid: String (UserId)
                 */
                case "/get-rxtx":
                    apiGetRxTx.h(request, clientSocket, origin);
                    break;

                /**
                 *  POST /get-conns
                 *  Expected JSON body 
                 *   - uid: String (UserId)
                 */
                case "/get-conns":
                    apiGetConns.h(request, clientSocket, origin);
                    break;
                
                /*****************
                 * ADMIN ENDPOINTS
                 *****************/

                /**
                 *  POST /get-total-users
                 *  Expected JSON body
                 *   - uid: String (UserId)
                 */
                case "/get-total-users":
                    apiGetTotalUsers.h(request, clientSocket, origin);
                    break;

                /**
                 *  POST /get-total-conns
                 *  Expected JSON body
                 *   - uid: String (UserId)
                 */
                case "/get-total-conns":
                    apiGetTotalConns.h(request, clientSocket, origin);
                    break;

                /**
                 *  POST /set-proxy-type
                 *  Expected JSON body
                 *   - uid: String (UserId)
                 *   - type: String (ProxyType)
                 */
                case "/set-proxy-type":
                    apiSetProxyType.h(request, clientSocket, origin);
                    break;
                
                /**
                 *  POST /set-proxy-auth
                 *  Expected JSON body
                 *   - uid: String (UserId)
                 *   - state: String (Boolean)
                 */
                case "/set-proxy-auth":
                    apiSetProxyAuth.h(request, clientSocket, origin);
                    break;

                default:
                    apiUtils.responses.resourceNotFound(clientSocket, origin);
                    break;
            }

            clientSocket.close();

        } catch (IOException io) {}

    }
    
}

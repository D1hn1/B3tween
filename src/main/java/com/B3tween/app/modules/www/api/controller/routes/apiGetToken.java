package com.B3tween.app.modules.www.api.controller.routes;

import java.net.Socket;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.auth.repository.authRepository;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.www.jwt.repository.jwtRepository;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.enums.Method;
import org.json.JSONObject;

public class apiGetToken {

    /**
     * Get token endpoint
     * @param request User request
     * @param clientSocket The client socket
     */
    public static void h(requestDto request, Socket clientSocket, String origin) {
        // OPTIONS Method
        if (request.getMethod().equals(Method.OPTIONS)) {
            apiUtils.responses.optionsResponse(clientSocket, origin);
            return;
        }
        // POST Method
        if (request.getMethod().equals(Method.POST)) {
            // Validate JWT
            if (jwtRepository.validateJWT(request)) {

                try {
                    // Get JSON body
                    JSONObject json = new JSONObject(request.getData());
                    int userId = Integer.parseInt(json.getString("uid").trim());
                    // Get user DTO
                    AuthDto user = authRepository.getUserById(userId);
                    if (user == null) {
                        Log.e("[API] (getUserById) user not found id=" + userId);
                        return;
                    }
                    // Send user token
                    apiUtils.responses.sendProxyTokenAndUsername(
                            clientSocket,
                            user.getProxyToken(),
                            user.getUsername(),
                            origin
                    );
                } catch (NumberFormatException nfe) {
                    Log.e(nfe.getMessage());
                    apiUtils.responses.internalError(clientSocket, origin);
                }
            } else {
                // Not authed user
                apiUtils.responses.forbiddenAuth(clientSocket, origin);
            }
        } else {
            // Method not allowed
            apiUtils.responses.methodNotAllowed(clientSocket, origin);
        }

    }

}

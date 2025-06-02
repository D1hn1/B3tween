package com.B3tween.app.modules.www.api.controller.routes;

import java.net.*;
import org.json.*;
import java.time.Instant;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.enums.Method;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.www.jwt.dto.JwtDto;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.auth.repository.authRepository;

public class apiLogin {
    
    /**
     * Login endpoint.
     * @param request User request.
     * @param clientSocket Client socket.
     */
    public static void h(requestDto request, Socket clientSocket) {
        // Parse method
        if (request.getMethod().equals(Method.POST)) {

            JSONObject json = new JSONObject(request.getData());
            String username = json.getString("username").trim();
            String password = json.getString("password").trim();

            if (username.isEmpty() || password.isEmpty()) {
                apiUtils.responses.loginConflict(clientSocket);
                return;
            }

            if (!authRepository.canUserLogin(username, password)) {
                apiUtils.responses.loginConflict(clientSocket);
                Log.e("[API] User failed logging username=" + username +
                    " from=" + clientSocket.getRemoteSocketAddress());
                return;
            }

            // Get the user
            AuthDto user = authRepository.getUser(username, password);

            // Create JWT Token
            JwtDto jwt = JwtDto.builder()
                .header("{ \"alg\": \"HS256\", \"typ\": \"JWT\" }")
                .payload("{\"username\":\"" + username + "\",\"id\":"+user.getId()+"}")
                .build();
            jwt.generateToken();

            // Set JWT to user DTO & update user dto
            user.setLoggedIn(true);
            user.setUpdatedAt(Instant.now().toEpochMilli());

            // Send response
            Log.l("[API] User logged in id=" + user.getId() + " username=" + user.getUsername());
            apiUtils.loginCorrectSetCookie(clientSocket,
                "b3cookie="+jwt.getToken(), "/dashboard");

        } else if (request.getMethod().equals(Method.OPTIONS)) {
            apiUtils.responses.optionsResponse(clientSocket);
        } else {
            apiUtils.responses.methodNotAllowed(clientSocket);
        }

    }

}

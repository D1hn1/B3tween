package com.B3tween.app.modules.www.api.controller.routes;

import java.net.*;
import org.json.*;
import java.time.Instant;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.enums.Method;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.proxy.token.ProxyToken;
import com.B3tween.app.modules.auth.repository.authRepository;

public class apiRegister {
    
    /**
     * Register endpoint.
     * @param request Client request.
     * @param clientSocket Client Socket.
     */
    public static void h(requestDto request, Socket clientSocket) {
        if (request.getMethod().equals(Method.POST)) {

            // Parse JSON
            JSONObject json = new JSONObject(request.getData());
            String username = json.getString("username").trim();
            String password = json.getString("password").trim();

            // Check for null values
            if (username.isEmpty() || password.isEmpty()) {
                apiUtils.responses.registerConflict(clientSocket);
                return;
            }

            // Check if user exists
            if (!authRepository.canUserRegister(username)) {
                apiUtils.responses.registerConflictUsername(clientSocket);
                return;
            }

            // Generate unique token
            String userToken = ProxyToken.generate();

            // Auth DTO
            AuthDto user = AuthDto.builder()
                .id(apiUtils.getNextUserId())
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .username(username)
                .password(password)
                .proxyToken(userToken)
                .build();

            // Save user
            Log.l("[API] User registered id=" + user.getId() + " username=" + user.getUsername());
            authRepository.save(user);

            // Response
            apiUtils.responses.foundRedirect(clientSocket, "/login");
        } else if (request.getMethod().equals(Method.OPTIONS)) {
            apiUtils.responses.optionsResponse(clientSocket);
        } else {
            // Method not valid
            apiUtils.responses.methodNotAllowed(clientSocket);
        }

    }

}

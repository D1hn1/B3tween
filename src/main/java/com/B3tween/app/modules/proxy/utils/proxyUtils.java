package com.B3tween.app.modules.proxy.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.List;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.auth.repository.authRepository;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.global.globalRuntime;

public class proxyUtils {

    /**
     * Retrieves the AuthDto from a request
     * @param request The user request
     * @return Null if the user is not found ||
     *         the AuthDto user if its found
     */
    public static AuthDto getAuthDtoFromRequest(requestDto request) {
        // Loop through request headers
        for (headerDto header : request.getHeaders()) {
            // Get authentication header
            if (header.getKey().equalsIgnoreCase("Proxy-Authorization")) {
                String authenticationType = header.getValue().split(" ")[0].trim();
                String authenticationCreds = header.getValue().split(" ")[1].trim();
                // Parse Basic auth
                if (authenticationType.equalsIgnoreCase("Basic")) {
                    byte[] credsDecBase64 = Base64.getDecoder().decode(authenticationCreds);
                    String proxyCredentials = new String(credsDecBase64);
                    String username = proxyCredentials.toString().split(":")[0].trim(); // TRIM THEM
                    if (!username.isEmpty()) {
                        return authRepository.getUser(username);
                    }
                }
                // Parse Bearer auth
                if (authenticationType.equalsIgnoreCase("Bearer")) {
                    String proxyToken = authenticationCreds;
                    if (proxyToken != null) {
                        return authRepository.getUserByToken(proxyToken);
                    }
                }
            }
        }
        // Return null if no user is found
        return null;
    }

    /**
     * Generate next id for the Connection DTO.
     * @return The next id.
     */
    public static int getNextId() {
        return globalRuntime.connectionId++;
    }

    /**
     * Get the first request from the client.
     * @param clientSocket Client socket.
     * @return The request parsed.
     */
    public static requestDto getRequest(Socket clientSocket) {

        try {

            // Booleans
            boolean isContentLength = false;

            // Get input stream from client
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Get request
            String line = "";
            int contentLength = 0;
            StringBuilder request = new StringBuilder();
            while ((line = clientIn.readLine()) != null && !line.isEmpty()) {
                if (line.toLowerCase().startsWith("content-length")) {
                    isContentLength = true;
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                request.append(line).append("\r\n");
            }

            if (isContentLength) {
                // Get body response
                char[] rawBody = new char[contentLength];
                clientIn.read(rawBody);

                // Append it to the response
                request.append(new String(rawBody));
            }

            // Parse and return request
            return requestDto.parseRequest(request.toString());

        } catch (IOException io) {
            Log.e("[PROXY] Error while getting user input " + io);
            return null;

        } catch (bException be) {
            Log.e("[PROXY] " + be.getMessage());
            return null;

        }
        
    }

    /**
     * Closes all the running connections
     */
    public static void closeAllConnections() {
        if (globalRuntime.connectionList != null) {
            globalRuntime.connectionList.forEach(conn -> {
                try {
                    conn.getClientSocket().close();
                } catch (IOException io) {};
            });
        }
        globalRuntime.threadPool.shutdown();
    }

    public static class responses {

        /**
         * Returns a 407 when the client is unauthorized
         * @param clientSocket The client socket
         */
        public static void proxyAuthenticationRequired(Socket clientSocket) {
            // HTTP/1.1 407 Proxy Authentication Required
            // Proxy-Authenticate: Basic realm="Proxy requires authentication"
            try {
                String data = "Bad credentials. Closing connection\n";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 407, 
                            "Proxy Authentication Required", 
                            List.of(headerDto.header("Proxy-Authenticate", "Basic realm=\"Proxy requires authentication\""),
                                    headerDto.header("Content-Length", ""+data.length())), 
                            data);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Returns a 502 when the server is unricheable.
         * @param clientSocket The client socket.
         */
        public static void proxyBadGateway(Socket clientSocket) {
            // HTTP/1.1 502 Bad Gateway
            //Content-Type: text/html
            //Content-Length: 123
            //502 Bad Gateway
            //Unable to connect to the host
            try {
                String data = "502 Bad Gateway\nUnable to connect to the host";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 502,
                            "Bad Gateway",
                            List.of(headerDto.header("Content-Type", "text/html"),
                                    headerDto.header("Content-Length", ""+data.length())),
                            data);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}

        }

        /**
         * Returns a 200 when the server connects to a http service correctly.
         * @param clientSocket The client socket.
         */
        public static void connectionEstablished(Socket clientSocket) {
            // 200 Connection Established
            // Proxy-Agent: B3tween/1.1
            try {
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 200, 
                    "Connection Established",
                    List.of(headerDto.header("Proxy-Agent", "B3tween/1.1")), null);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

    }

}
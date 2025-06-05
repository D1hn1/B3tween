package com.B3tween.app.modules.www.api.utils;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.global.globalRuntime;

public class apiUtils {

    /**
     * Get the list of CORS headers
     * @param origin The origin of the request
     * @return A list containing CORS Headers
     */
    private static List<headerDto> setCorsHeaders(String origin) {
        return List.of(headerDto.header("Access-Control-Allow-Origin", origin),
            headerDto.header("Access-Control-Allow-Credentials", "true"),
            headerDto.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS"),
            headerDto.header("Access-Control-Allow-Headers", "Content-Type"),
            headerDto.header("Connection", "Close"));
    }

    /**
     * Generates the next ID for the User.
     * @return The next ID.
     */
    public static int getNextUserId() {
        return globalRuntime.authId++;
    }

    /**
     * Get the origin where the request originated from.
     * @param request The user request.
     * @return A String containing the origin or "*" if not found.
     */
    public static String getOrigin(requestDto request) {
        Optional<headerDto> originHeader = request.getHeaders().stream().filter(header ->
            header.getKey().equalsIgnoreCase("origin"))
            .findFirst();
        return originHeader.isPresent() ?
            originHeader.get().getValue() :
            "*";
    }

    /**
     * Sends a 302 Found when a user logs in correctly.
     * And sets a cookie after the login.
     * @param clientSocket The client socket
     * @param cookie The cookie to set
     */
    public static void loginCorrectSetCookie(Socket clientSocket, int userId, String cookie, String location, String origin) {
        try {
            String data = "{\"location\":\""+location+"\",\"cookie\":\""+cookie+"\",\"status\":302,\"uid\":"+userId+"}";
            BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            responseDto response = responseDto.builder()
                .httpVersion("HTTP/1.1")
                .statusCode(302)
                .reasonPhrase("Found")
                .headers(new ArrayList<>(
                    List.of(headerDto.header("Content-Type", "application/json"),
                            headerDto.header("Content-Length", ""+data.length()))
                ))
                .data(data)
                .build();
            response.getHeaders().addAll(setCorsHeaders(origin));
            clientOut.write(response.toString());
            clientOut.flush();
        } catch (IOException io) {}
    }

    public static class responses {

        /**
         * Sends a 500 when something goes wrong
         * @param clientSocket The client socket
         */
        public static void internalError(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"Internal Server Error\", \"message\":\"An error has occurred\", \"status\":500}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                        .httpVersion("HTTP/1.1")
                        .statusCode(500)
                        .reasonPhrase("Internal Server Error")
                        .headers(new ArrayList<>(
                                List.of(headerDto.header("Content-Type", "application/json"),
                                        headerDto.header("Content-Length", ""+data.length()))
                        ))
                        .data(data)
                        .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 403 when a user has no access to a resource
         * @param clientSocket The client socket
         */
        public static void forbiddenAuth(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"Forbidden\", \"message\":\"User is not authorized\", \"status\":403}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                        .httpVersion("HTTP/1.1")
                        .statusCode(403)
                        .reasonPhrase("Forbidden")
                        .headers(new ArrayList<>(
                                List.of(headerDto.header("Content-Type", "application/json"),
                                        headerDto.header("Content-Length", ""+data.length()))
                        ))
                        .data(data)
                        .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 302 with the proxy token
         * @param clientSocket The client socket
         * @param proxyToken The client proxy token
         */
        public static void sendProxyToken(Socket clientSocket, String proxyToken, String origin) {
            try {
                String data = "{\"proxyToken\":\""+proxyToken+"\"}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                        .httpVersion("HTTP/1.1")
                        .statusCode(302)
                        .reasonPhrase("Found")
                        .headers(new ArrayList<>(
                                List.of(headerDto.header("Content-Type", "application/json"),
                                        headerDto.header("Content-Length", ""+data.length()))
                        ))
                        .data(data)
                        .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 200 Ok.
         * @param clientSocket The client socket.
         */
        public static void twoHundredOk(Socket clientSocket, String origin) {
            try {
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(200)
                    .reasonPhrase("Ok")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Connection", "close"))
                    ))
                    .data(null)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 302 when a user registers correctly
         * @param clientSocket The client socket.
         */
        public static void foundRedirect(Socket clientSocket, String redirect, String origin) {
            try {
                String data = "{\"location\":\""+redirect+"\"}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(302)
                    .reasonPhrase("Found")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Content-Type", "application/json"), 
                                headerDto.header("Content-Length", ""+data.length())) 
                    ))
                    .data(data)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 404 when a resource is not found.
         * @param clientSocket The client socket
         */
        public static void resourceNotFound(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"Not Found\", \"message\":\"The requested resource was not found\", \"status\":404}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(404)
                    .reasonPhrase("Not Found")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Content-Type", "application/json"),
                                headerDto.header("Content-Length", ""+data.length()))

                    ))
                    .data(data)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 405 when the method is not allowed.
         * @param clientSocket The client socket.
         */
        public static void methodNotAllowed(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"Method not allowed\", \"message\":\"This endpoint doesn't support this method\", \"status\":405}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(405)
                    .reasonPhrase("Method Not Allowed")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Content-Type", "application/json"), 
                                headerDto.header("Content-Length", ""+data.length()))
                    ))
                    .data(data)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 409 when a conflict occurs in registration
         * @param clientSocket The client socket
         */
        public static void registerConflict(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"An error ocurred while registering\"}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(409)
                    .reasonPhrase("Conflict")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Content-Type", "application/json"),
                                headerDto.header("Content-Length", ""+data.length()))

                    ))
                    .data(data)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 409 when a user is already created
         * @param clientSocket The client socket
         */
        public static void registerConflictUsername(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"User already exists\"}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(409)
                    .reasonPhrase("Conflict")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Content-Type", "application/json"),
                                headerDto.header("Content-Length", ""+data.length()))
                    ))
                    .data(data)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 409 when a conflict occurs in login
         * @param clientSocket The client socket
         */
        public static void loginConflict(Socket clientSocket, String origin) {
            try {
                String data = "{\"error\":\"An error ocurred while logging in\",\"status\":409}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(409)
                    .reasonPhrase("Conflict")
                    .headers(new ArrayList<>(
                        List.of(headerDto.header("Content-Type", "application/json"),
                                headerDto.header("Content-Length", ""+data.length()))
                    ))
                    .data(data)
                    .build();
                response.getHeaders().addAll(setCorsHeaders(origin));
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        public static void optionsResponse(Socket clientSocket, String origin) {
            try {
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.builder()
                    .httpVersion("HTTP/1.1")
                    .statusCode(200)
                    .reasonPhrase("Ok")
                    .headers(setCorsHeaders(origin))
                    .data(null)
                    .build();
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }
    }
    
}

package com.B3tween.app.modules.www.api.utils;

import java.io.*;
import java.net.*;
import java.util.List;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.responseDto;

public class apiUtils {

    public static class responses {

        /**
         * Sends a 200 Ok.
         * @param clientSocket The client socket.
         */
        public static void twoHundredOk(Socket clientSocket) {
            try {
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 200, 
                    "Ok", 
                    List.of(headerDto.header("Connection", "close")), 
                    null);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 404 when a resource is not found.
         * @param clientSocket The client socket
         */
        public static void resourceNotFound(Socket clientSocket) {
            try {
                String data = "{\"error\":\"Not Found\", \"message\":\"The requested resource was not found\", \"status\":404}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 404,
                            "Not Found",
                            List.of(headerDto.header("Content-Length", ""+data.length())),
                            data);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 405 when the method is not allowed.
         * @param clientSocket The client socket.
         */
        public static void methodNotAllowed(Socket clientSocket) {
            try {
                String data = "{\"error\":\"Method not allowed\", \"message\":\"This endpoint doesnt support this method\", \"status\":405}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 405,
                    "Method Not Allowed", 
                    List.of(headerDto.header("Content-Length", ""+data.length())), 
                    data);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

        /**
         * Sends a 409 when a conflict occurs in registration
         * @param clientSocket The client socket
         */
        public static void userAlreadyExists(Socket clientSocket) {
            try {
                String data = "{\"error\":\"An error ocurred while registering\"}";
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                responseDto response = responseDto.response("HTTP/1.1", 409,
                    "Conflict",
                    List.of(headerDto.header("Content-Length", ""+data.length())),
                    data);
                clientOut.write(response.toString());
                clientOut.flush();
            } catch (IOException io) {}
        }

    }
    
}

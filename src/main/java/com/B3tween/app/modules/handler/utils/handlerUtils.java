package com.B3tween.app.modules.handler.utils;

import java.io.*;
import java.net.*;
import java.util.List;
import com.B3tween.app.objects.dto.*;
import com.B3tween.app.modules.log.*;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.objects.global.globalRuntime;

public class handlerUtils {

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

            // Get input stream from client
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Get request
            String line = "";
            StringBuilder response = new StringBuilder();
            while (!(line = clientIn.readLine()).isEmpty()) {
                response.append(line).append("\r\n");
            }

            // Parse and return request
            return requestDto.parseRequest(response.toString());

        } catch (IOException io) {
            Log.e("Error while getting user input" + io);
            return null;

        } catch (bException be) {
            Log.e(be.getMessage());
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

package com.B3tween.app.modules.handler.handleClient;

import java.io.*;
import java.net.*;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.request.handleRequest;

import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.log.Log;

public class handler {
    
    /**
     * 
     */
    public static void handleClient(Socket clientSocket) {

        Log.i("Opening new thread for " + clientSocket.getRemoteSocketAddress());
        try {

            // client IN/OUT
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            StringBuilder request = new StringBuilder();
            String line;

            // get client request
            while ((line = clientIn.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                request.append(line);
                request.append("\r\n");
            }
            request.append("\r\n\r\n");

            // parse client request and send it
            requestDto requestDTO = requestDto.parseRequest(request.toString());
            handleRequest.handle(requestDTO, clientSocket);

        } catch (IOException e) {
            Log.c("Error: " + e);
        } catch (bException e) {
            Log.e("Error " + e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException io) {}
            
        }

    } 

}

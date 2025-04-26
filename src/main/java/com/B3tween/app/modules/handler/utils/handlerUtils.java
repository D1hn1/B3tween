package com.B3tween.app.modules.handler.utils;

import java.io.*;
import java.net.*;
import com.B3tween.app.objects.dto.*;
import com.B3tween.app.modules.log.*;
import com.B3tween.app.modules.exception.bException;

public class handlerUtils {

    public static requestDto getRequest(Socket clientSocket) {

        try {

            // Get input stream from client
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Get request
            String line;
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

}

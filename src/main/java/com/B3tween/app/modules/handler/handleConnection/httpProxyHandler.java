package com.B3tween.app.modules.handler.handleConnection;

import java.io.*;
import java.io.IOException;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.global.globalRuntime;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.handler.utils.handlerUtils;
import com.B3tween.app.modules.socket.initializeHttpSocket;
import com.B3tween.app.modules.handler.handleConnection.dto.connectionDto;

public class httpProxyHandler {
    
    public static void dispatchRequest(connectionDto connectionData) {

        try {
            // Initialize IO and Sockets 
            initializeHttpSocket forwardSocket =
                new initializeHttpSocket(connectionData.getRequest());

            InputStream serverInBytes = forwardSocket.socket.getInputStream();
            BufferedReader serverInText = forwardSocket.in;
            BufferedWriter serverOut = forwardSocket.out;

            BufferedReader clientIn = connectionData.getClientIn();
            OutputStream clientOutBytes = connectionData.getClientSocket().getOutputStream();
            BufferedWriter clientOutText = connectionData.getClientOut();

            requestDto request = connectionData.getRequest();

            // Delete Proxy-Authorization & Proxy-Connection from request
            request.getHeaders().removeIf(header -> header.getKey().equals("Proxy-Connection"));
            request.getHeaders().removeIf(header -> header.getKey().equals("Proxy-Authorization"));

            // Send request
            // TODO: MAKE SOMETHING FOR MODIFYING THE REQUEST
            // TODO: SET THE BYTESIN BYTESOUT ON THE CONNECTIONDTO
            serverOut.write(request.toString());
            serverOut.flush();

            // Get headers
            String line = "";
            int contentLength = 0;
            while (!(line = serverInText.readLine()).isEmpty()) {
                clientOutText.write(line);
                clientOutText.write("\r\n");
                if (line.split(":")[0].trim().equals("Content-Length")) {
                    contentLength =
                        Integer.parseInt(line.split(":")[1].trim());
                }
            }
            clientOutText.write("\r\n");
            clientOutText.flush();

            // Get & send response
            int bytesRead;
            byte[] bufferResponse = new byte[contentLength];
            while ((bytesRead = serverInBytes.read(bufferResponse)) != -1) {
                clientOutBytes.write(bufferResponse, 0, bytesRead);
            }
            clientOutBytes.flush();

            globalRuntime.connectionList.remove(connectionData);
            connectionData.getClientSocket().close();
            forwardSocket.closeSocket();

        } catch (IOException io)
        {} catch (bException e) {
            Log.e("Error: " + e);
        }

    }

}

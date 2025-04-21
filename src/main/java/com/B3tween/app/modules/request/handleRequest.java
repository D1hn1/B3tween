package com.B3tween.app.modules.request;

import java.io.*;
import java.net.*;

import java.io.IOException;

import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.enums.Exceptions;
import com.B3tween.app.modules.socket.initializeSocket;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.modules.log.Log;

public class handleRequest {
    // TODO: Change the User-Agent header for a custom one
    // TODO: Separate the function that reads from the server and others
    // TODO: Find a way so i can save and load values from the (disk drive or the ram) into the code
    /**
     * 
     */
    public static void handle(requestDto requestData, Socket clientSocket) throws bException {

        initializeSocket socket = new initializeSocket(requestData);

        // filter http/https
        switch (requestData.getMethod()) {

            case CONNECT:
                socket.closeSocket();
                throw new bException(Exceptions.HTTPS_NOT_IMPLEMENTED, "");
        
            default:

                boolean stopIterating = true;

                while (stopIterating) {

                    //try {
                    //    clientSocket.wait();
                    //}
                    //catch (InterruptedException ie) {
                    //    Log.e("Error: " + ie);
                    //    socket.closeSocket();
                    //}

                    socket.out.println(requestData.toString());

                    // parse response
                    responseDto parsedResponse = null;
                    StringBuilder response = new StringBuilder();

                    try {

                        String line;
                        while ((line = socket.in.readLine()) != null) {
                            if (line.isEmpty()) {
                                break;
                            }
                            response.append(line).append("\r\n");
                        } // MARK: SEE WHY IT DOESNT SAVE TO PARSEDRESPONSE ?????
                        parsedResponse = responseDto.parseResponse(response.toString());

                    } catch (IOException ie) {
                        Log.e("Error: " + ie);
                    }

                    // parse headers
                    int contentLength = 0;
                    //String transferEncoding;

                    boolean isContentLength = false;
                    boolean isTransferEncoding = false;

                    for (headerDto header : parsedResponse.getHeaders()) {

                        if (header.getKey().equals("Content-Length"))
                            contentLength = Integer.parseInt(header.getValue());
                            isContentLength = true;

                        if (header.getKey().equals("Transfer-Encoding"))
                            //transferEncoding = header.getValue();
                            isTransferEncoding = true;
                            Log.c("Error: transfer encoding not implemented");

                        if (header.getKey().equals("Connection"))
                            if (!header.getValue().equals("Keep-Alive"))
                                stopIterating = false;
                    };

                    // receive data
                    if (isContentLength && !isTransferEncoding) {                       
                        try {

                            InputStream input = socket.socket.getInputStream();
                            BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                            byte[] data = new byte[contentLength];
                            //int bytesReceive = 0;

                            while (input.read(data) != -1);

                            clientOut.write(data.toString());
                            clientOut.flush();
                            
                        } catch (IOException io) {
                            stopIterating = false;
                            socket.closeSocket();
                            Log.e("Error: " + io);
                        }

                    }
                }
                socket.closeSocket();
                try {
                    clientSocket.close();
                } catch (IOException io) {}
                break;
        }

    }

}

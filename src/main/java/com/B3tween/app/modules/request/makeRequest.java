package com.B3tween.app.modules.request;

import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.socket.initializeSocket;

import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.objects.enums.Exceptions;
import com.B3tween.app.modules.log.Log;

/**
 * Makes a request to a web server.
 * @throws bException If socket connection fails.
 * @return responseDto.
 */
public class makeRequest {
    
    public static void request(requestDto requestData) throws bException {

        // Log requests
        Log.i("Request sent to " + requestData.getURL().getProtocol() + "://" +
            requestData.getURL().getHost() + requestData.getURL().getPath());

        initializeSocket socket = new initializeSocket(requestData);

        // filter protocols (http, https)
        if (requestData.getURL().getProtocol() == "http") {

            // send request
            socket.send(requestData.toString());
            socket.recv().forEach(line -> {
                System.out.println(line);
            });

        } else if (requestData.getURL().getProtocol() == "https") {
            socket.closeSocket();
            throw new bException(Exceptions.HTTPS_NOT_IMPLEMENTED, "");
        }

        socket.closeSocket();
    }

}

package com.B3tween.app.modules.request;

import java.io.*;
import java.net.*;

//import com.B3tween.app.objects.dto.uriDto;
//import com.B3tween.app.objects.enums.Method;
//import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.dto.requestDto;

/**
 * Makes a request to a web server.
 * @return responseDto.
 */
public class makeRequest {
    
    public static void request(requestDto requestData) throws Exception {

        // set up socket conn
        String socketHost = requestData.getURL().getHost();
        int socketPort = requestData.getURL().getPort() != null ?
                         Integer.parseInt(requestData.getURL().getPort()) :
                         requestData.getURL().getProtocol() == "http" ? 80 : 443;

        Socket socket;

        try {
            socket = new Socket(socketHost, socketPort);
        } catch (UnknownHostException e) {
            System.err.println("Error " + e);
            throw new Exception("Error.Socket.Connection");
        }

        // filter protocols (http, https)
        if (requestData.getURL().getProtocol() == "http") {

            // filter methods (GET, POST, ...)
            switch (requestData.getMethod()) {
                
                case GET:

                    // set up request
                    String request = requestData.getMethod() + " " + requestData.getURL().getPath() + " " + requestData.getHttpVersion() + "\r\n" +
                                     "Host: " + requestData.getURL().getHost() + "\r\n" +
                                     "User-Agent: Mozilla/5.0\r\n\r\n";
                    
                    // send request
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    
                    out.println(request);
                    String response = in.readLine();
                    System.out.println(response);

                    break;

                default:
                    socket.close();
                    throw new Exception("Method.Not.Implemented");
            }

        } else if (requestData.getURL().getProtocol() == "https") {
            socket.close();
            throw new Exception("Https.Not.Implemented");
        }

        socket.close();
    }

}

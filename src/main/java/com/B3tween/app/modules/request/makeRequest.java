package com.B3tween.app.modules.request;

//import com.B3tween.app.objects.dto.uriDto;
//import com.B3tween.app.objects.enums.Method;
//import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.socket.initializeSocket;

/**
 * Makes a request to a web server.
 * @throws Exception If socket connection fails.
 * @return responseDto.
 */
public class makeRequest {
    
    public static void request(requestDto requestData) throws Exception {

        initializeSocket socket = new initializeSocket(requestData);

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
                    socket.send(request);
                    socket.recv().forEach(line -> {
                        System.out.println(line);
                    });

                    break;

                default:
                    socket.closeSocket();
                    throw new Exception("Method.Not.Implemented");
            }

        } else if (requestData.getURL().getProtocol() == "https") {
            socket.closeSocket();
            throw new Exception("Https.Not.Implemented");
        }

        socket.closeSocket();
    }

}

package com.B3tween.app.modules.request;

//import java.net.Socket;
import com.B3tween.app.objects.dto.uriDto;

/**
 * Makes a request to a web server.
 * @return responseDto.
 */
public class makeRequest {
    
    public static void get() {
        try {
            uriDto url = uriDto.url("http://google.com:300");
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

    }

}

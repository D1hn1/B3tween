package com.B3tween.app.modules.www.frontend.controller.routes;

import java.io.*;
import java.net.*;
import java.util.List;

import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.www.frontend.utils.webUtils;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.objects.enums.Method;

public class webIndex {
    
    public static void h(requestDto request, Socket clientSocket) {
        if (request.getMethod().equals(Method.GET)) {

            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String data = webUtils.readFile("./controller/resources/index.html");
                String[] cmd = {"pwd"};
                Runtime.getRuntime().exec(cmd);
                responseDto response = responseDto.response("HTTP/1.1", 200, "Ok",
                List.of(headerDto.header("Content-Length", ""+data.length()), headerDto.header("Connection", "Close")), data);
                writer.write(response.toString());
                writer.flush();
            } catch (IOException io) {}

        } else {
            apiUtils.responses.methodNotAllowed(clientSocket);
        }
    }

}

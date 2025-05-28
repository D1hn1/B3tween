package com.B3tween.app.modules.www.frontend;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.www.frontend.controller.frontController;
import com.B3tween.app.objects.global.globalRuntime;

public class frontListener {
    
    public frontListener(int port) throws IOException {
        // Initialize socket
        ServerSocket socket =  new ServerSocket(port);
        Log.i("[WEB] listener started on port " + port);

        while (globalRuntime.RUNNING) {
            // Accept clients
            Socket clientSocket = socket.accept();
            globalRuntime.threadPool.submit(() -> frontController.Handle(clientSocket));
        }

        // Close socket
        socket.close();
    }

}

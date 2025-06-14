package com.B3tween.app.modules.www.api;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.www.api.controller.apiController;
import com.B3tween.app.objects.global.globalRuntime;

public class apiListener {

    /**
     * Listen for api requests
     * @param port The port to listen on
     * @throws IOException If an error occurs while accepting clients
     */
    public apiListener(int port) throws IOException {
        // Initialize socket
        ServerSocket socket = new ServerSocket(port);
        Log.i("[API] listener started on port " + port);

        while (globalRuntime.RUNNING) {
            // Accept clients
            Socket clientSocket = socket.accept();
            globalRuntime.threadPool.submit(() -> apiController.Handle(clientSocket));
        }

        // Close socket
        socket.close();
    }

}

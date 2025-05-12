package com.B3tween.app.modules.www.api;

import java.io.*;
import java.net.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.www.api.routes.handleRoutes;
import com.B3tween.app.objects.global.globalRuntime;

public class apiListener {

    public apiListener(int port) throws IOException {
        // Initialize socket
        ServerSocket socket = new ServerSocket(port);
        Log.i("Api listening on port " + port);

        while (globalRuntime.RUNNING) {
            // Accept clients
            Socket clientSocket = socket.accept();
            globalRuntime.threadPool.submit(() -> handleRoutes.Handle(clientSocket));
        }

        // Close socket
        socket.close();
    }

}

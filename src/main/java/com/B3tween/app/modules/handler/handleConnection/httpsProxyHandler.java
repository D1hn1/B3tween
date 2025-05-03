package com.B3tween.app.modules.handler.handleConnection;

import com.B3tween.app.modules.log.Log;

import java.io.IOException;
import com.B3tween.app.modules.handler.handleConnection.dto.connectionDto;

public class httpsProxyHandler {
    
    public static void dispatchRequest(connectionDto connectionData) {

        try {
            Log.e("Https is not implemented");
            connectionData.getClientSocket().close();
        } catch (IOException io) {}

    }

}

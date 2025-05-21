package com.B3tween.app.modules.www.api.controller.routes;

import java.net.*;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.www.api.utils.apiUtils;

public class apiLogout {
    
    /**
     * Logout endpoint.
     * @param request User request.
     * @param clientSocket Client socket.
     */
    public static void h(requestDto request, Socket clientSocket) {
        apiUtils.responses.twoHundredOk(clientSocket);
    }

}

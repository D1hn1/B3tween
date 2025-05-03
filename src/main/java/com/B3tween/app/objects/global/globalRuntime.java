package com.B3tween.app.objects.global;

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import com.B3tween.app.modules.handler.handleConnection.dto.connectionDto;

@Setter
public class globalRuntime {

    // Global username/password
    public static String PROXY_USERNAME = "admin";
    public static String PROXY_PASSWORD = "admin";

    // Booleans
    public static boolean RUNNING = true;
    public static boolean PROXY_AUTHENTICATION = true;

    // connectionDto list
    public static int connectionId;
    public static List<connectionDto> connectionList = new ArrayList<>();

}

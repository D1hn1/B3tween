package com.B3tween.app.objects.global;

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.B3tween.app.modules.proxy.connection.dto.connectionDto;

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

    // Constants
    public static int PROXY_PORT = 8080;

    // Thread pool
    public static ExecutorService threadPool = Executors.newCachedThreadPool();

}

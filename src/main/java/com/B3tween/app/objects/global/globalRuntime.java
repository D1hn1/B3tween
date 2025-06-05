package com.B3tween.app.objects.global;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import lombok.Setter;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.proxy.controller.dto.connectionDto;
import com.B3tween.app.modules.proxy.controller.enums.ProxyType;

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

    // authDto list
    public static int authId;
    public static List<AuthDto> authList = new ArrayList<>();

    // Constants
    public static int PROXY_PORT = 8080;
    public static int API_PORT = 8000;
    public static int WEB_PORT = 8001;

    // Thread pool
    public static ExecutorService threadPool = Executors.newCachedThreadPool();

    // JWT Secret
    public static String JWTSecret = "secret-word";

    // Proxy token length
    public static int proxyTokenLength = 48;

    // Proxy selection
    public static ProxyType proxyType = ProxyType.FORWARD;

}

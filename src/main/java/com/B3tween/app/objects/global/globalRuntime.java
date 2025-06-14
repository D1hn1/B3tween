package com.B3tween.app.objects.global;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Setter;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.proxy.controller.dto.connectionDto;
import com.B3tween.app.modules.proxy.controller.enums.ProxyType;

@Setter
public class globalRuntime {

    // Global username/password
    public static String ADMIN_USERNAME = "b3admin";
    public static String ADMIN_PASSWORD = "b3admin";

    // Booleans
    public static boolean RUNNING = true;
    public static boolean PROXY_AUTHENTICATION = true;

    // connectionDto list
    public static int connectionId;
    public static List<connectionDto> connectionList = new CopyOnWriteArrayList<>();

    // authDto list
    public static int authId;
    public static List<AuthDto> authList = new CopyOnWriteArrayList<>();

    // Constants
    public static int PROXY_PORT = 8080;
    public static int API_PORT = 8000;
    public static int WEB_PORT = 8001;

    // Thread pool
    public static ExecutorService threadPool = Executors.newCachedThreadPool();

    // JWT Secret
    public static String JWTSecret = "secret-word";

    // Proxy token length
    public static int proxyTokenLength = 32;

    // Proxy selection
    public static ProxyType proxyType = ProxyType.FORWARD;

}

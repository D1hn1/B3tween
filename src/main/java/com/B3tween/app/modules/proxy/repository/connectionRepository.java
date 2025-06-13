package com.B3tween.app.modules.proxy.repository;

import java.util.stream.Collectors;
import com.B3tween.app.objects.global.globalRuntime;

public class connectionRepository {
    
    /**
     * Gets the number of connections a user has
     * @param uid The user ID
     * @return The number of connections filtered by user ID
     */
    public static int getNumberConnectionFromUID(int uid) {
        return globalRuntime.connectionList.stream()
                .filter(conn -> conn.getUserId() == uid)
                .collect(Collectors.toList()).size();
    }

}

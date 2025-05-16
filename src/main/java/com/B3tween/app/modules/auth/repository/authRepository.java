package com.B3tween.app.modules.auth.repository;

import com.B3tween.app.modules.auth.dto.authDto;
import com.B3tween.app.objects.global.globalRuntime;

public class authRepository {

    /**
     * Checks if an user can Register
     * @param username String username
     * @param password String password
     * @return True if the user can register
     */
    public static boolean canUserRegister(String username, String password) {
        return !globalRuntime.authList.stream().anyMatch(auth -> auth.username.equals(username)) &&
                !globalRuntime.authList.stream().anyMatch(auth -> auth.password.equals(password));
    }

//    public static authDto save(String username, String password) {
//
//    }

}

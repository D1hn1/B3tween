package com.B3tween.app.modules.auth.repository;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.objects.global.globalRuntime;

public class authRepository {

    /**
     * Checks if an user can Register
     * @param username String username
     * @param password String password
     * @return True if the user can register
     */
    public static boolean canUserRegister(String username, String password) {
        return !globalRuntime.authList.stream().anyMatch(auth -> auth.getUsername().equals(username));
    }

    /**
     * Saves the AuthDTO in to the list.
     * @param dto The user to save.
     */
    public static void save(AuthDto dto) {
        globalRuntime.authList.add(dto);
    }

}

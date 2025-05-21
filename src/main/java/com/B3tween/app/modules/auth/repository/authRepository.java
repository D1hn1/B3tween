package com.B3tween.app.modules.auth.repository;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.objects.global.globalRuntime;

public class authRepository {

    /**
     * Checks if an user can Register.
     * @param username String username.
     * @return True if the user can register.
     */
    public static boolean canUserRegister(String username) {
        return !globalRuntime.authList.stream().anyMatch(auth -> auth.getUsername().equals(username));
    }

    /**
     * Check if an user can Login.
     * @param username String username.
     * @param password String password.
     * @return True if the user can register.
     */
    public static boolean canUserLogin(String username, String password) {
        return globalRuntime.authList.stream().anyMatch(auth -> auth.getUsername().equals(username) &&
                auth.getPassword().equals(password));
    }

    /**
     * Saves the AuthDTO in to the list.
     * @param dto The user to save.
     */
    public static void save(AuthDto dto) {
        globalRuntime.authList.add(dto);
    }

    /**
     * Gets AuthDTto from username and password
     * @param username String username
     * @param password String password
     * @return Null if the list is empty or the user is not found
     *         or AuthDto the user DTO.
     */
    public static AuthDto getUser(String username, String password) {
        // Check if list is empty
        if (globalRuntime.authList.isEmpty())
            return null;
        // Loop through auth list
        for (AuthDto dto : globalRuntime.authList) {
            if (dto.getUsername().equals(username) && dto.getPassword().equals(password)) {
                return dto;
            }
        }
        // Return null if no user is founded
        return null;
    }

}

package com.B3tween.app.modules.auth.repository;

import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.proxy.token.ProxyToken;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.objects.global.globalRuntime;

import java.time.Instant;

public class authRepository {

    /* Create Admin User */
    public static void createAdminUser() {
        // Get admin proxy token
        String adminToken = ProxyToken.generate();
        // Admin DTO
        AuthDto adminUser = AuthDto.builder()
                .id(apiUtils.getNextUserId())
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .username(globalRuntime.ADMIN_USERNAME)
                .password(globalRuntime.ADMIN_PASSWORD)
                .proxyToken(adminToken)
                .build();
        // Save user
        save(adminUser);
        Log.l(String.format("Default admin account created - %s / %s",
                adminUser.getUsername(),
                adminUser.getPassword()));
    }

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
     * Gets AuthDto from username and password
     * @param username String username
     * @return Null if the list is empty or the user is not found
     *         or AuthDto the user DTO.
     */
    public static AuthDto getUser(String username) {
        // Check if list is empty
        if (globalRuntime.authList.isEmpty())
            return null;
        // Loop through auth list
        for (AuthDto dto : globalRuntime.authList) {
            if (dto.getUsername().equals(username)) {
                return dto;
            }
        }
        // Return null if no user is founded
        return null;
    }

    /**
     * Gets AuthDto from UID
     * @param uid The user ID
     * @return Null if the list is empty or the user is not found
     *         or AuthDto the user DTO.
     */
    public static AuthDto getUserById(int uid) {
        // Check if list is empty
        if (globalRuntime.authList.isEmpty())
            return null;
        // Loop through auth list
        for (AuthDto dto : globalRuntime.authList) {
            if (dto.getId() == uid) {
                return dto;
            }
        }
        // Return if no user is founded
        return null;
    }

    /**
     * Retrieves an user by its proxy token
     * @param token The user proxy token
     * @return Null if the list is empty or if the user is not found
     *         or AuthDto the user DTO.
     * @see {@link com.B3tween.app.modules.proxy.token}
     */
    public static AuthDto getUserByToken(String token) {
        // Check if list is empty
        if (globalRuntime.authList.isEmpty())
            return null;
        // Loop through auth list
        for (AuthDto dto : globalRuntime.authList) {
            if (dto.getProxyToken().equals(token)) {
                return dto;
            }
        }
        // Return if no user is founded
        return null;
    }

}

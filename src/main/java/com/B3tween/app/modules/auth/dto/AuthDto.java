package com.B3tween.app.modules.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;


@Getter
@Setter
@Builder
public class AuthDto {
    
    private int id;

    private Long createdAt;
    private Long updatedAt;

    private boolean isLoggedIn;

    private String username;
    private String password;

}

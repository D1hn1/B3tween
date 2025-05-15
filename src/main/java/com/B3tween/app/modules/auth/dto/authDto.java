package com.B3tween.app.modules.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;


@Getter
@Setter
@Builder
public class authDto {
    
    public int id;
    public String username;
    public String password;
    public String bearerToken;

}

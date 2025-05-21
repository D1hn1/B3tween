package com.B3tween.app.modules.www.jwt.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import com.B3tween.app.objects.global.globalRuntime;


@Getter
@Setter
@Builder
public class JwtDto {

    private String header;
    private String payload;
    private String token;

    public void generateToken() {
        if (token == null) {
            String b64EncodedHeader = Base64.getEncoder().encodeToString(header.getBytes()).replace("=","");
            String b64EncodedPayload = Base64.getEncoder().encodeToString(payload.getBytes()).replace("=","");

            String toDigest = b64EncodedHeader + b64EncodedPayload + globalRuntime.JWTSecret;
            String sha256Secret = DigestUtils.sha256Hex(toDigest);
            this.token = b64EncodedHeader + "." + b64EncodedPayload + "." + sha256Secret;
        }
    }

}

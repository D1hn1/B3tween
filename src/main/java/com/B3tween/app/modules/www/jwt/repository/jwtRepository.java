package com.B3tween.app.modules.www.jwt.repository;

import java.util.Base64;

import com.B3tween.app.objects.global.globalRuntime;
import org.json.JSONObject;
import com.B3tween.app.modules.www.jwt.dto.JwtDto;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.repository.authRepository;

public class jwtRepository {

    public static boolean isUserAdministrator(requestDto request) {
        for (headerDto header : request.getHeaders()) {
            if (header.getKey().equalsIgnoreCase("cookie")) {
                // Parse JWT
                String rawPay = header.getValue().split("=")[1].split("\\.")[1].trim();
                // Decode header && payload
                String decodedPay = new String(Base64.getDecoder().decode(rawPay));
                // Validate user
                JSONObject json = new JSONObject(decodedPay);
                if (json.getString("username").equals(globalRuntime.ADMIN_USERNAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validates a JWT
     * @param request The request containing the Cookie
     * @return True if valid false otherwise
     */
    public static boolean validateJWT(requestDto request) {
        // Parse headers
        for (headerDto header : request.getHeaders()) {
            // Get cookie header
            if (header.getKey().equalsIgnoreCase("cookie")) {
                // Parse JWT
                String rawHea = header.getValue().split("=")[1].split("\\.")[0].trim();
                String rawPay = header.getValue().split("=")[1].split("\\.")[1].trim();
                String rawSig = header.getValue().split("=")[1].split("\\.")[2].trim();
                // Decode header && payload
                String decodedHea = new String(Base64.getDecoder().decode(rawHea));
                String decodedPay = new String(Base64.getDecoder().decode(rawPay));
                // Validate user
                JSONObject json = new JSONObject(decodedPay);
                if (authRepository.getUser(json.getString("username")) == null)
                    return false;
                // Build new JWT
                JwtDto jwt = JwtDto.builder()
                    .header(decodedHea)
                    .payload(decodedPay)
                    .build();
                // Get signature
                jwt.generateToken();
                String trueSig = jwt.getToken().split("\\.")[2].trim();
                // Check authenticity 
                if (trueSig.equals(rawSig)) {
                    return true;
                }
            }
        }
        return false;
    }   

}

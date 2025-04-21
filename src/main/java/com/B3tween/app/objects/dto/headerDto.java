package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class headerDto {
    private String key;
    private String value;

    public static headerDto header(String key, String value) {
        return headerDto.builder()
                .key(key)
                .value(value)
                .build();
    }
}

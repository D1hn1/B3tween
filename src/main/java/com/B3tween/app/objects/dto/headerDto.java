package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

/**
 * Definition of DTO of headers
 * @return String
 */
@Getter
@Setter
@Builder
public class headerDto {
    private String key;
    private String value;
}

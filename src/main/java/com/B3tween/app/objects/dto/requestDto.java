package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.util.List;
import com.B3tween.app.objects.enums.Method;

/**
 * Definition of DTO request.
 * @return String, List<headerDto>, Method.
 */
@Getter
@Setter
@Builder
public class requestDto {
    private Method method;
    private String URI;
    private String httpVersion;
    private List<headerDto> headers;
    private String data;
}

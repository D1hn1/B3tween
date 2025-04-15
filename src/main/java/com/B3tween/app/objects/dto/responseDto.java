package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.util.List;

/**
 * Definition of DTO of response.
 * @return String, List<headerDto>.
 */
@Getter
@Setter
@Builder
public class responseDto {
    private String httpVersion;
    private String statusCode;
    private List<headerDto> headers;
    private String htmlCode;
}

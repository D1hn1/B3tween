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
    private uriDto URL;
    private String httpVersion;
    private List<headerDto> headers;
    private String data;

    public static requestDto request(Method method, uriDto URL, String httpVersion,
                                     List<headerDto> headers, String data) {

        return requestDto.builder()
                        .method(method)
                        .URL(URL)
                        .httpVersion(httpVersion != null ? httpVersion : "HTTP/1.1")
                        .headers(headers)
                        .data(data)
                        .build();
    }
}

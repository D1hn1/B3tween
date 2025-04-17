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

    /**
     * Request builder
     * @param method
     * @param URL
     * @param httpVersion
     * @param headers
     * @param data
     * @return requestDto
     */
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

    /**
     * Converts the request to string
     * @return String
     */
    public String toString() {

        StringBuilder request = new StringBuilder();

        request.append(method + " " + URL.getPath() + " " + httpVersion + "\r\n" +
                        "Host: " + URL.getHost() + "\r\n" +
                        "User-Agent: Mozilla/5.0\r\n");
        
        headers.forEach(header -> {
            request.append(header.getKey() + ": " + header.getValue() + "\r\n");
        });

        request.append("\r\n" + (data == null ? "" : data));

        return request.toString();
    }

}

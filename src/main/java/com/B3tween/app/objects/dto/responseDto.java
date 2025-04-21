package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import com.B3tween.app.modules.exception.bException;

@Getter
@Setter
@Builder
public class responseDto {
    private int statusCode;
    private String httpVersion;
    private String reasonPhrase;
    private List<headerDto> headers;
    private String data;

    /**
     * 
     * @param httpVersion
     * @param statusCode
     * @param reasonPhrase
     * @param headers
     * @param data
     * @return
     */
    public static responseDto response(String httpVersion, int statusCode, String reasonPhrase, 
                                        List<headerDto> headers, String data ) {

        return responseDto.builder()
                .reasonPhrase(reasonPhrase)
                .httpVersion(httpVersion)
                .statusCode(statusCode)
                .headers(headers)
                .data(data)
                .build();
    }

    /**
     * 
     */
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion + " " + statusCode + " " + reasonPhrase + "\r\n");
        headers.forEach(header -> {
            response.append(header.getKey() + ": " + header.getValue() + "\r\n");
        });
        response.append("\r\n" + (data == null ? "" : data));
        return response.toString();
    }

    /**
     * 
     */
    public static responseDto parseResponse(String response) {

        // responseDto fields
        int returnStatusCode = 0;
        String returnData = null;
        String returnHttpVersion = null;
        String returnReasonPhrase = null;
        List<headerDto> returnHeaders = new ArrayList<>();

        // response.httpVersion
        for (int x = 0; response.charAt(x) != ' '; x++) {
            if (response.charAt(x + 1) == ' ') {
                returnHttpVersion = response.substring(0, (x + 1));
            } 
        }

        // response.statusCode
        int offset = (returnHttpVersion.length() + 1);
        for (int x = offset; response.charAt(x) != ' '; x++) {
            if (response.charAt(x + 1) == ' ') {
                returnStatusCode = Integer.parseInt(response.substring(offset, (x + 1)).trim());
                offset = (offset + response.substring(offset, x).length() + 1);
            }
        }

        // response.reasonPhrase
        for (int x = offset; response.charAt(x) != '\r'; x++) {
            if (response.charAt(x + 1) == '\r') {
                returnReasonPhrase = response.substring(offset, (x + 1)).trim();
            }
        }

        // response.headers
        String key = null;
        String value = null;
        Boolean isKey = true;
        offset = (offset + returnReasonPhrase.length() + 2);
        for (int x = offset; x < response.toString().length(); x++) {

            if (isKey && response.charAt(x) == ':' && response.charAt(x + 1) == ' ') {
                key = response.substring(offset, x).trim();
                offset = (offset + key.length() + 2);
                isKey = false;
            }

            if (!isKey && response.charAt(x) == '\r' && response.charAt(x + 1) == '\n') {
                value = response.substring(offset, x).trim();
                offset = (offset + value.length() + 2);
                isKey = true;
                returnHeaders.add(headerDto.header(key, value));
            }

            if (response.charAt(x) == '\r' && response.charAt(x + 2) == '\r')
                break;

        }

        // response.data
        returnData = response.substring(offset, response.length()).trim();

        // return
        return responseDto.response(returnHttpVersion, returnStatusCode, returnReasonPhrase, returnHeaders, returnData);
    }
}

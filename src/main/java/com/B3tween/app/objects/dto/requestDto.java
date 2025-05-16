package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import com.B3tween.app.objects.enums.Exceptions;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.objects.enums.Method;

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
        request.append(method + " " + URL.getPath() + " " + httpVersion + "\r\n");
        headers.forEach(header -> {
            request.append(header.getKey() + ": " + header.getValue() + "\r\n");
        });
        request.append("\r\n" + (data == null ? "" : data)).append("\r\n");

        return request.toString();
    }

    /**
     * Parse request from raw String
     * @return requestDto
     * @throws bException
     */
    public static requestDto parseRequest(String request) throws bException {

        // requestDto fields
        String returnData = null;
        Method returnMethod = null;
        String returnHttpVersion = null;
        List<headerDto> returnHeaders = new ArrayList<>();
        
        // uriDto fields
        String returnProtocol = null;
        String returnHost = null;
        String returnPort = null;

        // uriDto if http || https
        uriDto returnUrlHttp = null;
        uriDto returnUrlHttps = null;
        
        // request.method
        for (int x = 0; request.charAt(x) != ' '; x++) {
            if (request.charAt(x + 1) == ' ') {
                try {
                    returnMethod = Method.getMethod(request.substring(0, (x + 1)));
                } catch (bException e) {
                    throw new bException(Exceptions.METHOD_NOT_SUPPORTED, "");
                }
            }
        }

        // request.URL.protocol
        returnProtocol = returnMethod == Method.CONNECT ? "https" : "http";

        // request.URL
        int offset = (returnMethod.getLabel().length() + 1);
        switch (returnMethod) {
            // https get host 
            case CONNECT:
                for (int x = offset; request.charAt(x) != ' '; x++) {
                    if (request.charAt(x + 1) == ' ') {
                        for (int y = offset; y < (x + 1); y++) {
                            if (request.charAt(y) == ':') {
                                returnHost = request.substring(offset, y);
                                returnPort = request.substring((y + 1), (x + 1));
                            }
                        }
                        offset = (offset + request.substring(offset, (x + 1)).length() + 1);
                    }
                }
                break;
        
            // http get url
            default:
                for (int x = offset; request.charAt(x) != ' '; x++) {
                    if (request.charAt(x + 1) == ' ') {
                        String urlOrPath = request.substring(offset, (x+1));
                        returnUrlHttp = uriDto.parseUrl(urlOrPath);
                        if (returnUrlHttp.getProtocol() == null) {
                            returnUrlHttp = uriDto.url(returnProtocol, null, null, urlOrPath);
                        }
                        offset = (offset + urlOrPath.length() + 1);
                    }
                }
                break;
        }

        // request.httpVersion
        for (int x = offset; request.charAt(x) != '\r'; x++) {
            if (request.charAt(x + 1) == '\r') {
                returnHttpVersion = request.substring(offset, (x + 1));
            }
        }

        // request.headers
        String key = null;
        String value = null;
        Boolean isKey = true;
        offset = (offset + returnHttpVersion.length() + 2);
        for (int x = offset; x < request.toString().length(); x++) {

            if (isKey && request.charAt(x) == ':' && request.charAt(x + 1) == ' ') {
                key = request.substring(offset, x);
                offset = (offset + key.length() + 2);
                isKey = false;
            }

            if (!isKey && request.charAt(x) == '\r' && request.charAt(x + 1) == '\n') {
                value = request.substring(offset, x);
                offset = (offset + value.length() + 2);
                isKey = true;
                returnHeaders.add(headerDto.header(key, value));
            }

        }

        // request.data
        returnData = request.substring(offset, request.length()).trim();

        // return
        switch (returnMethod) {
            case CONNECT:
                returnUrlHttps = uriDto.url(returnProtocol, returnHost, returnPort, "/");
                return requestDto.request(returnMethod, returnUrlHttps, returnHttpVersion, returnHeaders, returnData);
        
            default:
                return requestDto.request(returnMethod, returnUrlHttp, returnHttpVersion, returnHeaders, returnData);
        }
    }

}
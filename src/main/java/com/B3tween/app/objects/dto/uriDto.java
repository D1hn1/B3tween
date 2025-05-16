package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.util.List;

import com.B3tween.app.modules.exception.bException;

@Getter
@Setter
@Builder
public class uriDto {
    private String protocol;
    private String host;
    private String port;
    private String path;

    /**
     * 
     * @param protocol
     * @param host
     * @param port
     * @param path
     * @return
     */
    public static uriDto url(String protocol, String host, String port, String path) {
        return uriDto.builder()
            .protocol(protocol)
            .host(host)
            .port(port)
            .path(path)
            .build();
    }

    /**
     * 
     * @param URL
     * @return
     * @throws bException
     */
    public static uriDto parseUrl(String URL) throws bException {
        
        Boolean isPort = false;
        Boolean isPath = false;
        uriDto dto = new uriDto(null, null, null, null);
        int urlLength = URL.length();

        // dto.protocol
        dto.protocol = URL.startsWith("https") ? "https" :
                        URL.startsWith("http") ? "http" : null;

        if (dto.protocol == null)
            return dto;


        // dto.host
        int offset = (dto.protocol.length() + 3);

        for (int x = offset; x < urlLength; x++) {
            if (URL.charAt(x) == ':' || URL.charAt(x) == '/') {
                isPort = List.of(':').contains(URL.charAt(x)) ? true : false;
                dto.host = URL.substring(offset, x);
                break;
            } else {
                if ((offset + x) == urlLength) {
                    dto.host = URL.substring(offset, urlLength);
                }
            }
        }

        // dto.port
        if (isPort) {

            offset = (dto.host.length() + dto.protocol.length() + 4);

            for (int x = offset; x < urlLength; x++) {
                if (URL.charAt(x) == '/') {
                    isPath = List.of('/').contains(URL.charAt(x)) ? true : false;
                    dto.port = URL.substring(offset, x);
                    break;
                } else {
                    if ((x + 1) == urlLength) {
                        dto.port = URL.substring(offset, urlLength);
                    }
                }
            }

        }

        // dto.path
        if (isPath) {

            int offsetIfPort = isPort ? (dto.port.length() + 1) : 0;
            offset = (dto.host.length() + dto.protocol.length() + 3 + offsetIfPort);
            if (List.of('/').contains(URL.charAt(offset))) {
                dto.path = URL.substring(offset, urlLength);
            }
        } else {
            dto.path = "/";

        }

        return dto;

    }

}

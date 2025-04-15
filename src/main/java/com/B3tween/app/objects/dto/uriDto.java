package com.B3tween.app.objects.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Class: Definition the URI dto entity.
 * Function: Parses the URL with: protocol, host, port & path.
 * @throws Exception if the URL is malformed.
 * @return uriDto entity.
 */
@Getter
@Setter
public class uriDto {
    private String protocol;
    private String host;
    private String port;
    private String path;

    public static uriDto url(String URL) throws Exception {
        return parseUrl(URL);
    }

    private static uriDto parseUrl(String URL) throws Exception {
        
        Boolean isPort = false;
        Boolean isPath = false;
        uriDto dto = new uriDto();
        int urlLength = URL.length();

        // dto.protocol
        dto.protocol = URL.startsWith("https") ? "https" :
                        URL.startsWith("http") ? "http" : null;

        if (dto.protocol == null)
            throw new Exception("Malformed.Url");

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

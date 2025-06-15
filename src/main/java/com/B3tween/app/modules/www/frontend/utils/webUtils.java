package com.B3tween.app.modules.www.frontend.utils;

import java.io.*;
import java.util.*;
import java.net.Socket;
import java.nio.file.Files;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.responseDto;

public class webUtils {
 
    // Static resource path
    private static String staticPath = "src/main/java/com/B3tween/app/modules/www/frontend/controller/resources";

    /**
     * Check if file exists
     * @param rawFile Filename
     * @return True if file exists, false otherwise
     */
    public static boolean doFileExists(String rawFile) {
        // Get file handler
        File file = new File(staticPath + rawFile);
        // Check if file is a directory
        if (file.isDirectory())
            return false;
        // Check if file exists
        if (file.exists())
            return true;
        // File does not exists
        return false;
    }

    /**
     * Sends the favicon when requested
     * @param clientSocket The client socket
     * @param rawFavicon The path to the favicon
     * @throws IOException If an error occurs while writing to the client
     */
    public static void sendFavicon(Socket clientSocket, String rawFavicon) throws IOException {
        // Get favicon data
        File favicon = new File(staticPath + rawFavicon);
        OutputStream out = clientSocket.getOutputStream();
        byte[] iconBytes = Files.readAllBytes(favicon.toPath());
        responseDto response = responseDto.response("HTTP/1.1", 200, "Ok",
            List.of(headerDto.header("Content-Type", "image/x-icon"),
                    headerDto.header("Content-Length", ""+iconBytes.length),
                    headerDto.header("Connection", "close")),
            null);
        out.write(response.toString().getBytes());
        out.write(iconBytes);
        out.flush();
    }

    /**
     * Sends a 200 Ok with file
     * @param writer Client out stream
     * @param rawFile String filename
     * @throws IOException If an error occurs while writing to the client
     */
    public static void responseFound(BufferedWriter writer, String rawFile) throws IOException {
        // Get file data
        String file = readFile(rawFile);
        // Build response
        responseDto response = responseDto.response("HTTP/1.1", 200, "Ok",
            List.of(headerDto.header("Content-Length", ""+file.length()),
                    headerDto.header("Cache-Control", "no-store, no-cache, must-revalidate"),
                    headerDto.header("Pragma", "no-cache"),
                    headerDto.header("Expires", "0"),
                    headerDto.header("Conntection", "close")),
            file);
        // Send response
        writer.write(response.toString());
        writer.close();
    }

    /**
     * Sends a 400 Not Found with file
     * @param writer Client out stream
     * @param rawFile String filename
     * @throws IOException If an error occurs while writing to the client
     */
    public static void responseNotFound(BufferedWriter writer, String rawFile) throws IOException {
        // Get file data
        String file = readFile(rawFile);
        // Build response
        responseDto response = responseDto.response("HTTP/1.1", 404, "Not Found",
            List.of(headerDto.header("Content-Length", ""+file.length()),
                    headerDto.header("Cache-Control", "no-store, no-cache, must-revalidate"),
                    headerDto.header("Pragma", "no-cache"),
                    headerDto.header("Expires", "0"),
                    headerDto.header("Connection", "close")),
            file);
        // Send response
        writer.write(response.toString());
        writer.flush();
    }

    /**
     * Sends a 403 when a client is not authorized 
     * @param writer Client out stream
     * @param rawFile String filename
     * @throws IOException If an error occurs while writing to the client
     */
    public static void forbiddenAuth(BufferedWriter writer, String rawFile) throws IOException {
        // Get file data
        String file = readFile(rawFile);
        // Build response
        responseDto response = responseDto.response("HTTP/1.1", 403, "Forbidden",
            List.of(headerDto.header("Content-Length", ""+file.length()),
                    headerDto.header("Cache-Control", "no-store, no-cache, must-revalidate"),
                    headerDto.header("Pragma", "no-cache"),
                    headerDto.header("Expires", "0"),
                    headerDto.header("Connection", "close")),
            file);
        // Send response
        writer.write(response.toString());
        writer.flush();
    }

    /**
     * Sends a 405 when the method is not allowed.
     * @param writer The client output stream.
     * @param rawFile String filename
     * @throws IOException If an error occurs while writing to the client
     */
    public static void methodNotAllowed(BufferedWriter writer, String rawFile) throws IOException {
        // Get file data
        String file = readFile(rawFile);
        // Build resopnse
        responseDto response = responseDto.response("HTTP/1.1", 405, "Method Not Allowed",
                List.of(headerDto.header("Content-Length", ""+file.length()),
                        headerDto.header("Cache-Control", "no-store, no-cache, must-revalidate"),
                        headerDto.header("Pragma", "no-cache"),
                        headerDto.header("Expires", "0"),
                        headerDto.header("Connection", "close")),
                file);
        // Send response
        writer.write(response.toString());
        writer.flush();
    }

    /**
     * Send 302 and redirecting to user to another page.
     * @param writer The client output stream.
     * @param redirectTo The page to redirect.
     * @throws IOException If an error occurs while writing to the user.
     */
    public static void redirect(BufferedWriter writer, String redirectTo) throws IOException {
        responseDto response = responseDto.builder()
                .httpVersion("HTTP/1.1")
                .statusCode(302)
                .reasonPhrase("Found")
                .headers(new ArrayList<>(
                        List.of(headerDto.header("Location", redirectTo),
                                headerDto.header("Connection", "close"))
                ))
                .data(null)
                .build();
        writer.write(response.toString());
        writer.flush();
    }

    /**
     * Get the content from a file.
     * @param rawFile The file to get.
     * @return A string containing file data.
     */
    public static String readFile(String rawFile) {
        try {
            // Get file object
            File file = new File(staticPath + rawFile);
            Scanner reader = new Scanner(file);
            // Build response
            StringBuilder response = new StringBuilder();
            // Get file content
            while (reader.hasNextLine())
                response.append(reader.nextLine()).append("\r\n");
            // Close reader
            reader.close();
            // Return file
            return response.toString();
        } catch (FileNotFoundException fnf) {
            Log.e("[WEB] file was not found " + rawFile);
            return null;
        }
    }
}

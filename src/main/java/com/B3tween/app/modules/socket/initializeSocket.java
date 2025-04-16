package com.B3tween.app.modules.socket;

import java.io.*;
import java.net.*;
import java.util.stream.Stream;
import com.B3tween.app.objects.dto.requestDto;

/**
 * Initializes a Socket for better handling.
 * @throws Exception If connection fails.
 */
public class initializeSocket {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // constructor
    public initializeSocket(requestDto requestData) throws Exception {

        String socketHost = requestData.getURL().getHost();
        int socketPort = requestData.getURL().getPort() != null ?
                         Integer.parseInt(requestData.getURL().getPort()) :
                         requestData.getURL().getProtocol() == "http" ? 80 : 443;

        try {
            socket = new Socket(socketHost, socketPort);

        } catch (UnknownHostException ex) {
            throw new Exception("Error.Socket.Connection: " + ex);

        } catch (IOException iex) {
            throw new Exception("Error.Socket.Connection: " +iex);

        }

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    /**
     * Sends data through the socket.
     * @param request String.
     * @return Int
     */
    public int send(String request) {
        out.println(request);
        return 0;
    }

    /**
     * Receives data from the socket
     * @return Stream<String> (lines)
     */
    public Stream<String> recv() {
        return in.lines();
    }

    /**
     * Closes the opened socket.
     * @throws Exception If closing the socket fails.
     */
    public void closeSocket() throws Exception {
        try {
            socket.close();
        } catch (IOException iex) {
            throw new Exception("Socket.Close.Error: " + iex);
        }
    }

}

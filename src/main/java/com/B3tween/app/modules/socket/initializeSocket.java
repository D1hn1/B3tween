package com.B3tween.app.modules.socket;

import java.io.*;
import java.net.*;
import java.util.stream.Stream;
import com.B3tween.app.objects.dto.requestDto;

import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.objects.enums.Exceptions;

/**
 * Initializes a Socket for better handling.
 * @throws bException If connection fails.
 */
public class initializeSocket {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // constructor
    public initializeSocket(requestDto requestData) throws bException  {

        String socketHost = requestData.getURL().getHost();
        int socketPort = requestData.getURL().getPort() != null ?
                         Integer.parseInt(requestData.getURL().getPort()) :
                         requestData.getURL().getProtocol() == "http" ? 80 : 443;

        try {
            socket = new Socket(socketHost, socketPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (UnknownHostException ex) {
            throw new bException(Exceptions.UNKNOWN_HOST, ex.getMessage());

        } catch (IOException iex) {
            throw new bException(Exceptions.IO_CONN_ERR, iex.getMessage());

        }

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
    public void closeSocket() throws bException {
        try {
            socket.close();
        } catch (IOException iex) {
            throw new bException(Exceptions.SOCKET_CLOSE_ERROR, iex.getMessage());
        }
    }

}

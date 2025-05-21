package com.B3tween.app.modules.socket;

import java.io.*;
import java.net.*;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.log.Log;
import com.B3tween.app.modules.exception.bException;
import com.B3tween.app.objects.enums.Exceptions;

public class initializeSocket {

    public Socket socket;
    public BufferedWriter out;
    public BufferedReader in;

    /**
     * Http socket constructo
     * @param requestData the request from the client.
     * @throws bException If an error occurs while getting IO.
     */
    public initializeSocket(requestDto requestData, String clientAddr) throws bException  {

        String socketHost = requestData.getURL().getHost();
        int socketPort = requestData.getURL().getPort() != null ?
                         Integer.parseInt(requestData.getURL().getPort()) :
                         requestData.getURL().getProtocol() == "http" ? 80 : 443;
        
        Log.i("[PROXY] Client " + clientAddr + " -> " + requestData.getMethod().getLabel().toUpperCase()
            + " " + socketHost + ":" + socketPort);

        try {
            socket = new Socket(socketHost, socketPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (UnknownHostException ex) {
            throw new bException(Exceptions.UNKNOWN_HOST, ex.getMessage());

        } catch (IOException iex) {
            throw new bException(Exceptions.IO_CONN_ERR, iex.getMessage());

        }

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

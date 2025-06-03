package com.B3tween.app.modules.proxy.controller.dto;

import java.io.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.net.Socket;

import com.B3tween.app.objects.dto.requestDto;

@Getter
@Setter
@Builder
public class connectionDto {

    // Fields
    private int id;
    private requestDto request;
    private Socket clientSocket;
    private boolean isKeepAlive;
    private BufferedReader clientIn;
    private BufferedWriter clientOut;

}

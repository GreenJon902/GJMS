package com.greenjon902.gjms.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PlayerConnection {
    private final Socket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;

    public PlayerConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }
}

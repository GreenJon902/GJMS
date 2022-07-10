package com.greenjon902.gjms.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
    private final Socket socket;

    public final InputStream inputStream;
    public final OutputStream outputStream;
    public final String ip;

    protected Connection(Socket socket) throws IOException {
        this.socket = socket;

        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.ip = socket.getInetAddress() + ":" + socket.getPort();
    }

    public Connection(InputStream inputStream, OutputStream outputStream, String ip) {
        this.socket = null;

        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.ip = ip;
    }
}

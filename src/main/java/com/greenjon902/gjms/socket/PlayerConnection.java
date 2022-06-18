package com.greenjon902.gjms.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A container class to store everything to do with the player's connection, it means other modules of GJMS don't have
 * to use a socket.
 */
public class PlayerConnection {
    private final Socket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;
    public final String ip;

    public PlayerConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        ip =  socket.getInetAddress() + ":" + socket.getPort();
    }
}

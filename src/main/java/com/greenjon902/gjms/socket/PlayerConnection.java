package com.greenjon902.gjms.socket;

import java.net.Socket;

public class PlayerConnection {
    private final Socket socket;

    public PlayerConnection(Socket socket) {
        this.socket = socket;
    }
}

package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.connection.packetAdapter.HandshakePacketAdapter;
import com.greenjon902.gjms.connection.packetAdapter.PacketAdapter;

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

    private Class<? extends PacketAdapter> packetAdapter = HandshakePacketAdapter.class; // first packetAdapter
    private ConnectionState connectionState = ConnectionState.HANDSHAKE; // first packetAdapter

    public final InputStream inputStream;
    public final OutputStream outputStream;
    public final String ip;

    public PlayerConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        ip =  socket.getInetAddress() + ":" + socket.getPort();
    }

    public Class<? extends PacketAdapter> getPacketAdapter() {
        return packetAdapter;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }
}

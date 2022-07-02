package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.HandshakePacketAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

/**
 * A container class to store everything to do with a connection that is not in the play state.
 */
public class PrePlayConnection extends Connection {
    private PacketAdapter packetAdapter = HandshakePacketAdapter.getInstance(); // first packetAdapter
    private PrePlayConnectionState prePlayConnectionState = PrePlayConnectionState.HANDSHAKE;

    public PrePlayConnection(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * Gets this {@link PrePlayConnection}'s {@link PacketAdapter}.
     *
     * @return The packet adapter
     */
    public @NotNull PacketAdapter getPacketAdapter() {
        return packetAdapter;
    }

    /**
     * Gets this {@link PrePlayConnection}'s {@link PrePlayConnectionState}.
     *
     * @return The connection state
     */
    public PrePlayConnectionState getPrePlayConnectionState() {
        return prePlayConnectionState;
    }
}

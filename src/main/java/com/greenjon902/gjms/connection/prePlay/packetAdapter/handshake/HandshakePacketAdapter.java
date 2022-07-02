package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake;

import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Contains the functionality for converting packets when the client's
 * {@link PrePlayConnectionState} is {@link PrePlayConnectionState#HANDSHAKE}.
 */
public class HandshakePacketAdapter extends PrePlayPacketAdapter {
    static final PacketAdapter instance = new HandshakePacketAdapter();

    /**
     * Gets the usable instance of the {@link PacketAdapter}.
     *
     * @return The instance of the packet adapter
     */
    public static @NotNull PacketAdapter getInstance(){
        return instance;
    }
}

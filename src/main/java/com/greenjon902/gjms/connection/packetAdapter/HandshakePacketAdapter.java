package com.greenjon902.gjms.connection.packetAdapter;

import com.greenjon902.gjms.connection.ConnectionState;
import com.greenjon902.gjms.connection.PlayerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Contains the functionality for converting packets when the client's {@link ConnectionState} is
 * {@link ConnectionState#HANDSHAKE}.
 */
public class HandshakePacketAdapter extends PacketAdapter {
    static final PacketAdapter instance = new HandshakePacketAdapter();

    /**
     * Gets the usable instance of the {@link PacketAdapter}. Should be used to store in
     * {@link PlayerConnection#packetAdapter}.
     *
     * @return The instance of the packet adapter
     */
    public static @NotNull PacketAdapter getInstance(){
        return instance;
    }
}

package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapterSelector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

/**
 * A container class to store everything to do with a connection that is not in the play state.
 */
public class PrePlayConnection extends Connection {
    private int protocolVersion = -1; // by default is not set, is editable in prePlay as we don't know it yet
    private PacketAdapter packetAdapter;
    private PrePlayConnectionState prePlayConnectionState = PrePlayConnectionState.HANDSHAKE;
    public final boolean cracked;

    public String name;
    public long timestamp;
    public byte[] publicKey;
    public byte[] signature;

    public PrePlayConnection(Socket socket, boolean cracked) throws IOException {
        super(socket);
        this.cracked = cracked;
        updatePacketAdaptor(PrePlayConnectionState.HANDSHAKE, -1);
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

    /**
     * Gets this {@link PrePlayConnection}'s {@link #protocolVersion}.
     *
     * @return The protocol version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }


    /**
     * Updates this {@link PrePlayConnection}'s {@link #packetAdapter} to the correct one specified by the parameters.
     *
     * @param state           The {@link PrePlayConnectionState} of the connection
     * @param protocolVersion The connection's protocol version
     */
    public void updatePacketAdaptor(PrePlayConnectionState state, int protocolVersion) {
        System.out.println("Updating packetAdapter for self - " + this + " | state=" + state + " protocolVersion=" +
                protocolVersion);
        this.packetAdapter = PrePlayPacketAdapterSelector.selectAdapter(state, protocolVersion);
        this.prePlayConnectionState = state;
        this.protocolVersion = protocolVersion;
    }
}

package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.common.Connection;
import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapterSelector;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * A container class to store everything to do with a connection that is not in the play state.
 */
public class PrePlayConnection implements Connection {
    private final Socket socket;

    public final InputStream inputStream;
    public final OutputStream outputStream;

    private int protocolVersion = -1; // by default is not set, is editable in prePlay as we don't know it yet
    private PacketAdapter packetAdapter;
    private PrePlayConnectionState prePlayConnectionState = PrePlayConnectionState.HANDSHAKE;
    public final boolean cracked;

    public String name;
    public long timestamp;
    public byte[] publicKey;
    public byte[] signature;

    public PrePlayConnection(Socket socket, boolean cracked) throws IOException {
        this.socket = socket;

        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();

        this.cracked = cracked;
        updatePacketAdaptor(PrePlayConnectionState.HANDSHAKE, -1);
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

    @Override
    public void send(ClientboundPacket clientboundPacket) throws IOException {
        System.out.println("Sending " + clientboundPacket);
        byte[] encoded = packetAdapter.encodePacket(clientboundPacket);
        System.out.println(Arrays.toString(encoded));
        System.out.println(new String(encoded));
        outputStream.write(encoded);
    }

    @Override
    public @Nullable ServerboundPacket receive() throws IOException {
        if (inputStream.available() == 0) {
            return null; // No new packets available
        }

        ByteArrayInputStream packetInputStream = new ByteArrayInputStream(packetAdapter.readNextPacketFrom(inputStream));
        ServerboundPacket serverboundPacket = packetAdapter.decodePacket(packetInputStream);
        System.out.println("Received " + serverboundPacket);
        return serverboundPacket;
    }

    @Override
    public String getConnectionSourceName() {
        return "PrePlayConnection>Socket{" + socket.getLocalAddress()  + ":" + socket.getLocalPort() + " <--> " +
                socket.getInetAddress() + ":" + socket.getPort() + "}";
    }

    public Socket getSocket() {
        return socket;
    }
}

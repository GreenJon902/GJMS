package com.greenjon902.gjms.connection.play;

import com.greenjon902.gjms.common.Player;
import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.play.packetAdapter.PacketAdapterSelector;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

/**
 * A {@link Player} that comes from a socket which is adapted using {@link PacketAdapter}s which are selected from
 * {@link PacketAdapterSelector}. <br>
 * This is most likely the only implementation of a {@link Player} but there may be others for very special reasons.
 */
public class SocketPlayerImpl implements Player {
    private final UUID playerId;
    private String name;

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private final PacketAdapter packetAdapter;

    public SocketPlayerImpl(UUID playerId, String name, Socket socket, int protocolVersion) throws IOException {
        this.playerId = playerId;
        this.name = name;

        this.socket = socket;
        this.inputStream = this.socket.getInputStream();
        this.outputStream = this.socket.getOutputStream();

        this.packetAdapter = PacketAdapterSelector.selectAdapter(protocolVersion);
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
            return null;
        }

        ByteArrayInputStream packetInputStream = new ByteArrayInputStream(packetAdapter.readNextPacketFrom(inputStream));
        ServerboundPacket serverboundPacket = packetAdapter.decodePacket(packetInputStream);
        System.out.println("Received " + serverboundPacket);
        return serverboundPacket;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

import java.util.Arrays;

public class EncryptionRequest implements ClientboundPacket {
    public final String serverId;
    public final byte[] publicKey;
    public final byte[] verifyToken;

    public EncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    /**
     * The packet id is the way that minecraft clients are able to understand what each packet is and what it will do.
     *
     * @return The packet id of this packet
     */
    @Override
    public int getPacketId() {
        return 1;
    }

    @Override
    public String toString() {
        return "EncryptionRequest{" +
                "serverId=" + serverId +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", verifyToken=" + Arrays.toString(verifyToken) +
                '}';
    }
}

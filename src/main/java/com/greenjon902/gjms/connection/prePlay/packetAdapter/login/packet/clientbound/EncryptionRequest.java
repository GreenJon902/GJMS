package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

public class EncryptionRequest implements ClientboundPacket {
    public final String serverId;
    public final byte[] publicKey;
    public final byte[] verifyKey;

    public EncryptionRequest(String severId, byte[] publicKey, byte[] verifyKey) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyKey = verifyKey;
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
                "publicKey=" + publicKey +
                "verifyKey=" + verifyKey +
                '}';
    }
}

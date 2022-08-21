package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

import java.util.Arrays;

public class EncryptionRequest extends ClientboundPacket {
    public final String serverId;
    public final byte[] publicKey;
    public final byte[] verifyToken;

    public EncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

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

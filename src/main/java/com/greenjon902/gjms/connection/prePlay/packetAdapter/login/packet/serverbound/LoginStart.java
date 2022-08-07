package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

public class LoginStart implements ServerboundPacket {
    public final String name;
    public final bool hasSignatureData;
    public final long timestamp;
    public final byte[] publicKey;
    public final byte[] signature;

    public LoginStart(String name) {
        this(name, null, null, null);

        this.hasSignatureData = false;
    }

    public LoginStart(String name, long timestamp, byte[] publicKey, byte[] signature) {
        this.name = name;
        this.timestamp = timestamp;
        this.publicKey = publickey;
        this.signature = signature;

        this.hasSignatureData = true;
    }

    /**
     * The packet id is the way that minecraft clients are able to understand what each packet is and what it will do.
     *
     * @return The packet id of this packet
     */
    @Override
    public int getPacketId() {
        return 0;
    }

    @Override
    public String toString() {
        if (hasSignatureData) {
            return "EncryptionRequest{" +
                    "name=" + name +
                    "hasSignatureData=" + hasSignatureData +
                    "timestamp=" + timestamp +
                    "publicKey=" + publicKey +
                    "signature=" + signature +
                    '}';
        } else {
            return "LoginStart{" +
                    "name=" + name +
                    "hasSignatureData=" + hasSignatureData +
                    '}';
        }
    }
}

package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

import java.util.Arrays;
import java.util.Objects;

/**
 * A packet sent from the client that contains the client's name and encryption information (optional).
 */
public class LoginStart implements ServerboundPacket {
    public final String name;
    public final Long timestamp;
    public final byte[] publicKey;
    public final byte[] signature;

    public LoginStart(String name) {
        this(name, null, null, null);
    }

    public LoginStart(String name, Long timestamp, byte[] publicKey, byte[] signature) {
        this.name = name;
        this.timestamp = timestamp;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public boolean hasSignatureData() {
        return (Objects.nonNull(timestamp));
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
        if (hasSignatureData()) {
            return "EncryptionRequest{" +
                    "name=" + name +
                    "timestamp=" + timestamp +
                    "publicKey=" + Arrays.toString(publicKey) +
                    "signature=" + Arrays.toString(signature) +
                    '}';
        } else {
            return "LoginStart{" +
                    "name=" + name +
                    '}';
        }
    }
}

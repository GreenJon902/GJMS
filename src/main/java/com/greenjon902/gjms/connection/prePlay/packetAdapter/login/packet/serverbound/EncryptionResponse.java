package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

import java.util.Arrays;
/**
 * A packet sent from the client that contains the client's message encryption information. There are two forms of this,
 * there can either be a {@link #sharedSecret} and a {@link #verifyToken} or there can be a {@link #sharedSecret}, a
 * {@link #salt} and a {@link #messageSignature}.
 */
public class EncryptionResponse implements ServerboundPacket {
    public final byte[] sharedSecret;
    public final byte[] verifyToken;
    public final Long salt;
    public final byte[] messageSignature;

    public EncryptionResponse(byte[] sharedSecret, byte[] verifyToken) {
        this(sharedSecret, verifyToken, null, null);
    }

    public EncryptionResponse(byte[] sharedSecret, Long salt, byte[] messageSignature) {
        this(sharedSecret, null, salt, messageSignature);
    }

    public EncryptionResponse(byte[] sharedSecret, byte[] verifyToken, Long salt, byte[] messageSignature) {
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
        this.salt = salt;
        this.messageSignature = messageSignature;
    }

    public boolean hasVerifyToken() {
        return !(verifyToken == null);
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
        if (hasVerifyToken()) {
            return "EncryptionResponse{" +
                    "sharedSecret=" + Arrays.toString(sharedSecret) +
                    ", verifyToken=" + Arrays.toString(verifyToken) +
                    '}';
        } else {
            return "EncryptionResponse{" +
                    "sharedSecret=" + Arrays.toString(sharedSecret) +
                    ", salt=" + salt +
                    ", messageSignature=" + Arrays.toString(messageSignature) +
                    '}';
        }
    }
}

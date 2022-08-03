package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

public class SetCompression implements ClientboundPacket {
    public final int threshold;

    public SetCompression(int threshold) {
        this.threshold = threshold;
    }

    /**
     * The packet id is the way that minecraft clients are able to understand what each packet is and what it will do.
     *
     * @return The packet id of this packet
     */
    @Override
    public int getPacketId() {
        return 3;
    }

    @Override
    public String toString() {
        return "SetCompression{" +
                "threshold=" + threshold +
                '}';
    }
}

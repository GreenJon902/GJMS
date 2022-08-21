package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

public class SetCompression extends ClientboundPacket {
    public final int threshold;

    public SetCompression(int threshold) {
        this.threshold = threshold;
    }

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

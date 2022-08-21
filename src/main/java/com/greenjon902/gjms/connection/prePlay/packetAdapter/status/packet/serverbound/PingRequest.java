package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

public class PingRequest extends ServerboundPacket {
    public final long payload;

    public PingRequest(long payload) {
        this.payload = payload;
    }

    @Override
    public int getPacketId() {
        return 1;
    }

    @Override
    public String toString() {
        return "PingRequest{" +
                "payload=" + payload +
                '}';
    }
}

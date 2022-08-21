package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

public class PingResponse extends ClientboundPacket {
    public final long payload;

    public PingResponse(long payload) {
        this.payload = payload;
    }

    @Override
    public int getPacketId() {
        return 1;
    }

    @Override
    public String toString() {
        return "PingResponse{" +
                "payload=" + payload +
                '}';
    }
}

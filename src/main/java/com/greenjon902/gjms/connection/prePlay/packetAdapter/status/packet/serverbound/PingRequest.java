package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

public class PingRequest implements ServerboundPacket {
    public final long payload;

    public PingRequest(long payload) {
        this.payload = payload;
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
        return "PingRequest{" +
                "payload=" + payload +
                '}';
    }
}

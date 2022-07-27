package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.ServerboundPacket;

public class PingResponse implements ClientboundPacket {
    public final long payload;

    public PingResponse(long payload) {
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
        return "PingResponse{" +
                "payload=" + payload +
                '}';
    }
}

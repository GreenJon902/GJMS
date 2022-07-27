package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

public class StatusRequest implements ServerboundPacket {
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
        return "StatusRequest{}";
    }
}

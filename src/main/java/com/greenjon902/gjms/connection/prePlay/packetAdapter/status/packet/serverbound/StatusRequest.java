package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;

public class StatusRequest extends ServerboundPacket {

    @Override
    public int getPacketId() {
        return 0;
    }

    @Override
    public String toString() {
        return "StatusRequest{}";
    }
}

package com.greenjon902.gjms.connection.play.packetAdapter;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.ServerboundPacket;

import java.io.IOException;
import java.io.InputStream;

public class PacketAdapter760 extends PacketAdapter {
    @Override
    public ServerboundPacket decodePacket(InputStream inputStream) throws IOException {
        int packetId = decodeFirstVarInt(inputStream);
        throw new RuntimeException("Unknown packet with ID " + packetId);
    }

    @Override
    public byte[] encodePacket(ClientboundPacket clientboundPacket) {
        throw new RuntimeException("PacketAdapter760 cannot encode packet of type " +
                clientboundPacket.getClass().toString());
    }
}

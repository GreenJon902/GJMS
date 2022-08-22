package com.greenjon902.gjms;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.ServerboundPacket;

import java.io.IOException;
import java.io.InputStream;

public class TestPacketAdapter extends PacketAdapter {
    public static TestPacketAdapter instance = new TestPacketAdapter();

    @Override
    public ServerboundPacket decodePacket(InputStream inputStream) throws IOException {
        throw new RuntimeException("com.greenjon902.gjms.TestPacketAdapter.decodePacket has not been implemented");
    }

    @Override
    public byte[] encodePacket(ClientboundPacket clientboundPacket) {
        throw new RuntimeException("com.greenjon902.gjms.TestPacketAdapter.encodePacket has not been implemented");
    }
}

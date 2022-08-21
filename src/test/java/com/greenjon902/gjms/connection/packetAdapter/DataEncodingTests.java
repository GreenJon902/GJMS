package com.greenjon902.gjms.connection.packetAdapter;

import com.greenjon902.gjms.TestPacketAdapter;
import com.greenjon902.gjms.connection.PacketAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static com.greenjon902.gjms.Utils.byteArray;

public class DataEncodingTests {
    @Test
    public void testEncodeVarInt() {
        // Run + Check --
        Assertions.assertArrayEquals(byteArray(0x00), TestPacketAdapter.instance.encodeVarInt(0));
        Assertions.assertArrayEquals(byteArray(0x01), TestPacketAdapter.instance.encodeVarInt(1));
        Assertions.assertArrayEquals(byteArray(0x02), TestPacketAdapter.instance.encodeVarInt(2));
        Assertions.assertArrayEquals(byteArray(0x7f), TestPacketAdapter.instance.encodeVarInt(127));
        Assertions.assertArrayEquals(byteArray(0x80, 0x01), TestPacketAdapter.instance.encodeVarInt(128));
        Assertions.assertArrayEquals(byteArray(0xff, 0x01), TestPacketAdapter.instance.encodeVarInt(255));
        Assertions.assertArrayEquals(byteArray(0xdd, 0xc7, 0x01), TestPacketAdapter.instance.encodeVarInt(25565));
        Assertions.assertArrayEquals(byteArray(0xff, 0xff, 0x7f), TestPacketAdapter.instance.encodeVarInt(2097151));
        Assertions.assertArrayEquals(byteArray(0xff, 0xff, 0xff, 0xff, 0x07), TestPacketAdapter.instance.encodeVarInt(2147483647));
        Assertions.assertArrayEquals(byteArray(0xff, 0xff, 0xff, 0xff, 0x0f), TestPacketAdapter.instance.encodeVarInt(-1));
        Assertions.assertArrayEquals(byteArray(0x80, 0x80, 0x80, 0x80, 0x08), TestPacketAdapter.instance.encodeVarInt(-2147483648));
    }

    @Test
    public void testEncodeLong() {
        // Run + Check --
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(0L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01), TestPacketAdapter.instance.encodeLong(1L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00), TestPacketAdapter.instance.encodeLong(256L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(65536L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(16777216L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(4294967296L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(1099511627776L));
        Assertions.assertArrayEquals(byteArray(0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(281474976710656L));
        Assertions.assertArrayEquals(byteArray(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(72057594037927936L));
        Assertions.assertArrayEquals(byteArray(0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01), TestPacketAdapter.instance.encodeLong(72340172838076673L));
        Assertions.assertArrayEquals(byteArray(0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF), TestPacketAdapter.instance.encodeLong(9223372036854775807L));
        Assertions.assertArrayEquals(byteArray(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF), TestPacketAdapter.instance.encodeLong(-1L));
        Assertions.assertArrayEquals(byteArray(0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeLong(-9223372036854775808L));
    }

    @Test
    public void testEncodeString() {
        // Run + Check --
        Assertions.assertArrayEquals(byteArray(0x05, 'H', 'e', 'l', 'l', 'o'), TestPacketAdapter.instance.encodeString("Hello"));
        Assertions.assertArrayEquals(byteArray(0x0C, 'H', 'o', 'w', ' ', 'a', 'r', 'e', ' ', 'y', 'o', 'u', '?'), TestPacketAdapter.instance.encodeString("How are you?"));
        Assertions.assertArrayEquals(byteArray(0x00), TestPacketAdapter.instance.encodeString(""));
    }

    @Test
    public void testEncodeByteArray() {
        // Run + Check --
        Assertions.assertArrayEquals(byteArray(0x0B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), TestPacketAdapter.instance.encodeBytes(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)));
        Assertions.assertArrayEquals(byteArray(0x04, 0x05, 0xAA, 0xF3, 0xFD), TestPacketAdapter.instance.encodeBytes(byteArray(0x05, 0xAA, 0xF3, 0xFD)));
        Assertions.assertArrayEquals(byteArray(0x00), TestPacketAdapter.instance.encodeBytes(new byte[0]));
    }

    @Test
    public void testEncodeUUID() {
        // Run + Check --
        Assertions.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                           0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                TestPacketAdapter.instance.encodeUUID(new UUID(0L, 0L)));
        Assertions.assertArrayEquals(byteArray(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
                                           0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF),
                TestPacketAdapter.instance.encodeUUID(new UUID(-1L, -1L)));
        Assertions.assertArrayEquals(byteArray(0x5D, 0xE9, 0x74, 0xE9, 0x90, 0xF4, 0x11, 0x21,
                                           0x0D, 0x74, 0xED, 0x1D, 0x51, 0xC8, 0x25, 0x49),
                TestPacketAdapter.instance.encodeUUID(new UUID(6767068461608997153L, 969660529973405001L)));
        Assertions.assertArrayEquals(byteArray(0x00, 0x87, 0xB7, 0x73, 0x75, 0xAB, 0x4F, 0x56,
                                           0x45, 0x86, 0xB2, 0xA9, 0x9D, 0x25, 0xA4, 0x97),
                TestPacketAdapter.instance.encodeUUID(new UUID(38200828379221846L, 5009888077051962519L)));
    }
}

package com.greenjon902.gjms.connection.packetAdapter;

import com.greenjon902.gjms.connection.PacketAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static com.greenjon902.gjms.Utils.byteArray;

public class DataEncodingTests {
    @Test
    public void testEncodeVarInt() {
        // Run + Check --
        Assert.assertArrayEquals(byteArray(0x00), PacketAdapter.encodeVarInt(0));
        Assert.assertArrayEquals(byteArray(0x01), PacketAdapter.encodeVarInt(1));
        Assert.assertArrayEquals(byteArray(0x02), PacketAdapter.encodeVarInt(2));
        Assert.assertArrayEquals(byteArray(0x7f), PacketAdapter.encodeVarInt(127));
        Assert.assertArrayEquals(byteArray(0x80, 0x01), PacketAdapter.encodeVarInt(128));
        Assert.assertArrayEquals(byteArray(0xff, 0x01), PacketAdapter.encodeVarInt(255));
        Assert.assertArrayEquals(byteArray(0xdd, 0xc7, 0x01), PacketAdapter.encodeVarInt(25565));
        Assert.assertArrayEquals(byteArray(0xff, 0xff, 0x7f), PacketAdapter.encodeVarInt(2097151));
        Assert.assertArrayEquals(byteArray(0xff, 0xff, 0xff, 0xff, 0x07), PacketAdapter.encodeVarInt(2147483647));
        Assert.assertArrayEquals(byteArray(0xff, 0xff, 0xff, 0xff, 0x0f), PacketAdapter.encodeVarInt(-1));
        Assert.assertArrayEquals(byteArray(0x80, 0x80, 0x80, 0x80, 0x08), PacketAdapter.encodeVarInt(-2147483648));
    }

    @Test
    public void testEncodeLong() {
        // Run + Check --
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(0L));
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01), PacketAdapter.encodeLong(1L));
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00), PacketAdapter.encodeLong(256L));
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00), PacketAdapter.encodeLong(65536L));
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(16777216L));
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(4294967296L));
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(1099511627776L));
        Assert.assertArrayEquals(byteArray(0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(281474976710656L));
        Assert.assertArrayEquals(byteArray(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(72057594037927936L));
        Assert.assertArrayEquals(byteArray(0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01), PacketAdapter.encodeLong(72340172838076673L));
        Assert.assertArrayEquals(byteArray(0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF), PacketAdapter.encodeLong(9223372036854775807L));
        Assert.assertArrayEquals(byteArray(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF), PacketAdapter.encodeLong(-1L));
        Assert.assertArrayEquals(byteArray(0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeLong(-9223372036854775808L));
    }

    @Test
    public void testEncodeString() {
        // Run + Check --
        Assert.assertArrayEquals(byteArray(0x05, 'H', 'e', 'l', 'l', 'o'), PacketAdapter.encodeString("Hello"));
        Assert.assertArrayEquals(byteArray(0x0C, 'H', 'o', 'w', ' ', 'a', 'r', 'e', ' ', 'y', 'o', 'u', '?'), PacketAdapter.encodeString("How are you?"));
        Assert.assertArrayEquals(byteArray(0x00), PacketAdapter.encodeString(""));
    }

    @Test
    public void testEncodeByteArray() {
        // Run + Check --
        Assert.assertArrayEquals(byteArray(0x0B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00), PacketAdapter.encodeBytes(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)));
        Assert.assertArrayEquals(byteArray(0x04, 0x05, 0xAA, 0xF3, 0xFD), PacketAdapter.encodeBytes(byteArray(0x05, 0xAA, 0xF3, 0xFD)));
        Assert.assertArrayEquals(byteArray(0x00), PacketAdapter.encodeBytes(new byte[0]));
    }

    @Test
    public void testEncodeUUID() {
        // Run + Check --
        Assert.assertArrayEquals(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                           0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                PacketAdapter.encodeUUID(new UUID(0L, 0L)));
        Assert.assertArrayEquals(byteArray(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
                                           0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF),
                PacketAdapter.encodeUUID(new UUID(-1L, -1L)));
        Assert.assertArrayEquals(byteArray(0x5D, 0xE9, 0x74, 0xE9, 0x90, 0xF4, 0x11, 0x21,
                                           0x0D, 0x74, 0xED, 0x1D, 0x51, 0xC8, 0x25, 0x49),
                PacketAdapter.encodeUUID(new UUID(6767068461608997153L, 969660529973405001L)));
        Assert.assertArrayEquals(byteArray(0x00, 0x87, 0xB7, 0x73, 0x75, 0xAB, 0x4F, 0x56,
                                           0x45, 0x86, 0xB2, 0xA9, 0x9D, 0x25, 0xA4, 0x97),
                PacketAdapter.encodeUUID(new UUID(38200828379221846L, 5009888077051962519L)));
    }
}

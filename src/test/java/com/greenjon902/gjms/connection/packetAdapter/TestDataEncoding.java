package com.greenjon902.gjms.connection.packetAdapter;

import com.greenjon902.gjms.connection.PacketAdapter;
import org.junit.Assert;
import org.junit.Test;

import static com.greenjon902.gjms.Utils.byteArray;

public class TestDataEncoding {
    @Test
    public void encodeVarInt() {
        // Check --
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
    public void encodeLong() {
        // Check --
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
}

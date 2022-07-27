package com.greenjon902.gjms.connection;

import org.junit.Assert;
import org.junit.Test;

import static com.greenjon902.gjms.Utils.byteArray;

public class TestDataEncoding {
    @Test
    public void EncodeVarInt() {
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
}

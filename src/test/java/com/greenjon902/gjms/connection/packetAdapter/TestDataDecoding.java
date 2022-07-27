package com.greenjon902.gjms.connection.packetAdapter;

import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.PacketAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.greenjon902.gjms.Utils.byteArray;

public class TestDataDecoding {
    @Test
    public void decodeVarInt() throws IOException {
        // Setup ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(byteArray(0x00)); // 0
        outputStream.write(byteArray(0x01)); // 1
        outputStream.write(byteArray(0x02)); // 2
        outputStream.write(byteArray(0x7f)); // 127
        outputStream.write(byteArray(0x80, 0x01)); // 128
        outputStream.write(byteArray(0xff, 0x01)); // 255
        outputStream.write(byteArray(0xdd, 0xc7, 0x01)); // 25565
        outputStream.write(byteArray(0xff, 0xff, 0x7f)); // 2097151
        outputStream.write(byteArray(0xff, 0xff, 0xff, 0xff, 0x07)); // 2147483647
        outputStream.write(byteArray(0xff, 0xff, 0xff, 0xff, 0x0f)); // -1
        outputStream.write(byteArray(0x80, 0x80, 0x80, 0x80, 0x08)); // -2147483648

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Connection connection = new Connection(inputStream, null, null);

        int[] out = new int[11];

        // Run --
        for (int i=0; i<11; i++) {
            out[i] = PacketAdapter.decodeFirstVarInt(connection);
        }

        // Check --
        Assert.assertEquals(0, out[0]);
        Assert.assertEquals(1, out[1]);
        Assert.assertEquals(2, out[2]);
        Assert.assertEquals(127, out[3]);
        Assert.assertEquals(128, out[4]);
        Assert.assertEquals(255, out[5]);
        Assert.assertEquals(25565, out[6]);
        Assert.assertEquals(2097151, out[7]);
        Assert.assertEquals(2147483647, out[8]);
        Assert.assertEquals(-1, out[9]);
        Assert.assertEquals(-2147483648, out[10]);
    }

    @Test
    public void decodeString() throws IOException {
        // Setup ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(4);
        outputStream.write("test".getBytes(StandardCharsets.UTF_8));
        outputStream.write(30);
        outputStream.write("i did not fail my english exam".getBytes(StandardCharsets.UTF_8));
        outputStream.write(26);
        outputStream.write("Hello, how are you my guy?".getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Connection connection = new Connection(inputStream, null, null);

        String[] out = new String[3];

        // Run --
        for (int i=0; i<3; i++) {
            out[i] = PacketAdapter.decodeFirstString(connection);
        }

        // Check --
        Assert.assertEquals("test", out[0]);
        Assert.assertEquals("i did not fail my english exam", out[1]);
        Assert.assertEquals("Hello, how are you my guy?", out[2]);
    }

    @Test
    public void decodeFirstUnsignedShort() throws IOException {
        // Setup ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(byteArray(0x00, 0x00)); // 0
        outputStream.write(byteArray(0x00, 0x01)); // 1
        outputStream.write(byteArray(0x01, 0x00)); // 16
        outputStream.write(byteArray(0x01, 0x01)); // 17
        outputStream.write(byteArray(0x7F, 0xFF)); // 32767
        outputStream.write(byteArray(0x80, 0x00)); // 32768
        outputStream.write(byteArray(0xFF, 0xFF)); // 65535

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Connection connection = new Connection(inputStream, null, null);

        int[] out = new int[7];

        // Run --
        for (int i=0; i<7; i++) {
            out[i] = PacketAdapter.decodeFirstUnsignedShort(connection);
        }

        // Check --
        Assert.assertEquals(0, out[0]);
        Assert.assertEquals(1, out[1]);
        Assert.assertEquals(256, out[2]);
        Assert.assertEquals(257, out[3]);
        Assert.assertEquals(32767, out[4]);
        Assert.assertEquals(32768, out[5]);
        Assert.assertEquals(65535, out[6]);
    }

    @Test
    public void decodeFirstLong() throws IOException {
        // Setup ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)); // 0
        outputStream.write(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01)); // 1
        outputStream.write(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00)); // 256
        outputStream.write(byteArray(0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00)); // 65536
        outputStream.write(byteArray(0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00)); // 16777216
        outputStream.write(byteArray(0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00)); // 4294967296
        outputStream.write(byteArray(0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00)); // 1099511627776
        outputStream.write(byteArray(0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)); // 281474976710656
        outputStream.write(byteArray(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)); // 72057594037927936
        outputStream.write(byteArray(0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01)); // 72340172838076673
        outputStream.write(byteArray(0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF)); // 9223372036854775807
        outputStream.write(byteArray(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF)); // -1
        outputStream.write(byteArray(0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)); // -9223372036854775808

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Connection connection = new Connection(inputStream, null, null);

        long[] out = new long[13];

        // Run --
        for (int i=0; i<13; i++) {
            out[i] = PacketAdapter.decodeFirstLong(connection);
        }

        // Check --
        Assert.assertEquals(0L, out[0]);
        Assert.assertEquals(1L, out[1]);
        Assert.assertEquals(256L, out[2]);
        Assert.assertEquals(65536L, out[3]);
        Assert.assertEquals(16777216L, out[4]);
        Assert.assertEquals(4294967296L, out[5]);
        Assert.assertEquals(1099511627776L, out[6]);
        Assert.assertEquals(281474976710656L, out[7]);
        Assert.assertEquals(72057594037927936L, out[8]);
        Assert.assertEquals(72340172838076673L, out[9]);
        Assert.assertEquals(9223372036854775807L, out[10]);
        Assert.assertEquals(-1L, out[11]);
        Assert.assertEquals(-9223372036854775808L, out[12]);
    }
}
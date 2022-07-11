package com.greenjon902.gjms.connection;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDataDecoding {
    @Test
    public void DecodeVarInt() throws IOException {
        // Setup ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x00);
        outputStream.write(0x01);
        outputStream.write(0x02);
        outputStream.write(0x7f);
        outputStream.write(0x80); outputStream.write(0x01);
        outputStream.write(0xff); outputStream.write(0x01);
        outputStream.write(0xdd); outputStream.write(0xc7); outputStream.write(0x01);
        outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0x7f);
        outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0x07);
        outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0xff); outputStream.write(0x0f);
        outputStream.write(0x80); outputStream.write(0x80); outputStream.write(0x80); outputStream.write(0x80); outputStream.write(0x08);

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
    public void DecodeString() throws IOException {
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
}
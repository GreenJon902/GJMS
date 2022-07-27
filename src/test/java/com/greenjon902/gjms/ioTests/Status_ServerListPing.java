package com.greenjon902.gjms.ioTests;

import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.greenjon902.gjms.Utils.byteArray;

public class Status_ServerListPing {
    public final String ip = "127.0.0.1";
    public final short port = 25565;

    public void makeServer() {
        NewConnectionHandler socketManager = new NewConnectionHandler(port);
        socketManager.start();
        PrePlayConnectionHandler.startNewHandler();
    }

    @Test
    public void MC1_19() throws IOException {
        // Setup ---
        makeServer();
        Socket socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        long currentTime = System.currentTimeMillis();

        byte[] packets = byteArray(
                // Handshake ------------------------------
                // Length - VarInt
                0x10, // 00010000
                // Packet ID - VarInt
                0x00, // 00000000

                // Protocol Version - VarInt
                0xf6, // 11110110
                0x05, // 00000101
                // Server Address - String (255)
                0x09, // 00001001
                '1', '2', '7', '.', '0', '.', '0', '.', '1',
                // Server Port - Unsigned Short
                0x63, // 01100011
                0xdd, // 11011101
                // Next State - VarInt
                0x01, // 00000001

                // Status Request -------------------------
                // Length - VarInt
                0x01, // 00000001
                // Packet ID - VarInt
                0x00, // 00000000

                // Ping Request -------------------------
                // Length - VarInt
                0x03, // 00000003
                // Packet ID - VarInt
                0x01, // 00000001
                // Payload - Long
                (byte) (currentTime >> 56),
                (byte) (currentTime >> 48),
                (byte) (currentTime >> 40),
                (byte) (currentTime >> 32),
                (byte) (currentTime >> 24),
                (byte) (currentTime >> 16),
                (byte) (currentTime >> 8),
                (byte) currentTime
        );

        // Run ---
        outputStream.write(packets);

        // Check ---
        // Status Response -

        // Packet length
        while (true) { // TODO: Check packet length
            byte currentByte = (byte) inputStream.read();
            if ((currentByte & 0x80) == 0) break;
        }

        // Packet ID
        Assert.assertEquals("Wrong PacketId for StatusResponse", 0, inputStream.read());

        // JSON Response
        int jsonResponseLength = 0;
        int position = 0;
        while (true) {
            byte currentByte = (byte) inputStream.read();
            jsonResponseLength |= (currentByte & 0x7F) << position;
            if ((currentByte & 0x80) == 0) break;
            position += 7;
        }
        byte[] bytes = new byte[jsonResponseLength];
        inputStream.read(bytes);
        String jsonResponse = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("Server Status was " + jsonResponse + " as length was " + jsonResponseLength);
        // TODO: Check json


        // Ping Response -

        // Packet length
        Assert.assertEquals("Wrong PacketLength for PingResponse", 9, inputStream.read());

        // Packet ID
        Assert.assertEquals("Wrong PacketId for PingResponse", 1, inputStream.read());

        // Payload
        long payload = 0;
        for (int i=0;i<8;i++) {
            payload <<= 8;
            payload |= inputStream.read() & 0xFF;
        }
        Assert.assertEquals("Wrong Payload for PingResponse", currentTime, payload);
    }
}

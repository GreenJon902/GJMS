package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestHandshake {
    public final String ip = "127.0.0.1";
    public final short port = 25565;

    public void makeServer() {
        NewConnectionHandler socketManager = new NewConnectionHandler(port);
        socketManager.start();
        PrePlayConnectionHandler.startNewHandler();
    }

    @Test
    public void MC1_19() throws IOException, InterruptedException {
        // Setup ---
        makeServer();
        Socket socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        int[] packet = {
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
                0x01 // 00000001

        };

        // Run ---
        for (int data : packet) {
            outputStream.write(data);
        }

        Thread.sleep(1000); // Wait for server to have updated

        // Check ---
        Assert.assertEquals(0, inputStream.available());
        Assert.assertEquals(1, PrePlayConnectionHandler.getConnections().length);
        Assert.assertEquals(PrePlayConnectionState.STATUS, PrePlayConnectionHandler.getConnections()[0].getPrePlayConnectionState());
    }
}

package com.greenjon902.gjms;

import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.prePlay.PrePlayConnectionHandler;

import java.io.IOException;
import java.io.InputStream;

/**
 * The tools that a lot of tests will require
 */
public class Utils {
    public static byte[] byteArray(int... arr) {
        byte[] byteArray = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            byteArray[i] = (byte) arr[i];
        }
        return byteArray;
    }

    private static Integer port = null;
    /**
     * Make a server on any port if not yet made.
     * @return The port
     */
    public static int makeServer(boolean cracked) {
        if (port == null) {
            NewConnectionHandler socketManager = new NewConnectionHandler(cracked);
            socketManager.start();
            PrePlayConnectionHandler.startNewHandler();
            port = socketManager.getPort();
        }
        return port;
    }

    public static int readVarInt(InputStream inputStream) throws IOException {
        int value = 0;
        int position = 0;
        while (true) {
            byte currentByte = (byte) inputStream.read();
            value |= (currentByte & 0x7F) << position;
            if ((currentByte & 0x80) == 0) break;
            position += 7;
        }
        return value;
    }
}

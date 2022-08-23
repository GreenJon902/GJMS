package com.greenjon902.gjms;

import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.FirstWorldGetter;
import com.greenjon902.gjms.common.Player;
import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

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
            ConnectionHandler prePlayConnectionHandler = new PrePlayConnectionHandler(new FirstWorldGetterImpl());
            NewConnectionHandler newConnectionHandler = new NewConnectionHandler(cracked, prePlayConnectionHandler);
            newConnectionHandler.startNewHandler();
            prePlayConnectionHandler.startNewHandler();

            port = newConnectionHandler.getPort();
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

class FirstWorldGetterImpl implements FirstWorldGetter {
    @Override
    public String getWorld(UUID playerId) {
        return null;
    }
}
package com.greenjon902.gjms;

import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;

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

    /**
     * Make a server on any port.
     * @return The port
     */
    public static int makeServer() {
        NewConnectionHandler socketManager = new NewConnectionHandler();
        socketManager.start();
        PrePlayConnectionHandler.startNewHandler();
        return socketManager.getPort();
    }
}

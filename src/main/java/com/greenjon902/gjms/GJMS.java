package com.greenjon902.gjms;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;
import com.greenjon902.gjms.connection.NewConnectionHandler;

public class GJMS {
    public static void main(String[] args) {
        System.out.println("GJMS Starting...");
        System.out.println("Version is 1.0-SNAPSHOT");

        NewConnectionHandler socketManager = new NewConnectionHandler(25565, true);
        socketManager.start();
        PrePlayConnectionHandler.startNewHandler();
    }
}

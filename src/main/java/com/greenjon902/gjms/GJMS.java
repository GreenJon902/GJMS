package com.greenjon902.gjms;

import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandlerImpl;

public class GJMS {
    public static void main(String[] args) {
        System.out.println("GJMS Starting...");
        System.out.println("Version is 1.0-SNAPSHOT");

        ConnectionHandler prePlayConnectionHandler = new PrePlayConnectionHandlerImpl();

        NewConnectionHandler newConnectionHandler = new NewConnectionHandler(25565, true, prePlayConnectionHandler);
        newConnectionHandler.startNewHandler();

        prePlayConnectionHandler.startNewHandler();
    }
}

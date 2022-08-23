package com.greenjon902.gjms;

import com.greenjon902.gjms.worldHandler.WorldHandlerImpl;
import com.greenjon902.gjms.common.Storage;
import com.greenjon902.gjms.common.WorldHandler;

public class GJMS {
    public static final WorldHandler worldHandler = new WorldHandlerImpl();
    public static final Storage storage = null;

    /*
    public static void main(String[] args) {
        System.out.println("GJMS Starting...");
        System.out.println("Version is 1.0-SNAPSHOT");

        ConnectionHandler prePlayConnectionHandler = new PrePlayConnectionHandler();

        NewConnectionHandler newConnectionHandler = new NewConnectionHandler(25565, true, prePlayConnectionHandler);
        newConnectionHandler.startNewHandler();

        prePlayConnectionHandler.startNewHandler();
    }
    */
}

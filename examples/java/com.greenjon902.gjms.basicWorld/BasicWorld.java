package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.GJMS;
import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.World;
import com.greenjon902.gjms.common.WorldHandler;
import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;
import com.greenjon902.gjms.worldHandler.WorldHandlerImpl;

public class BasicWorld {
    public static void main(String... args) {
        World basicWorld = new WorldImpl(GJMS.worldHandler);

        ConnectionHandler prePlayConnectionHandler = new PrePlayConnectionHandler(new FirstWorldGetterImpl(),
                new ServerListPingStatusGetterImpl());

        NewConnectionHandler newConnectionHandler = new NewConnectionHandler(25565, true,
                prePlayConnectionHandler);

        newConnectionHandler.startNewHandler();
        prePlayConnectionHandler.startNewHandler();
    }
}

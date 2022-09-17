package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.GJMS;
import com.greenjon902.gjms.basicWorld.RegistryCodecImpl.DimensionTypeRegistryEntryElementImpl;
import com.greenjon902.gjms.basicWorld.RegistryCodecImpl.DimensionTypeRegistryEntryImpl;
import com.greenjon902.gjms.basicWorld.RegistryCodecImpl.RegistryCodecImpl;
import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.RegistryCodec.DimensionTypeRegistryEntry;
import com.greenjon902.gjms.common.World;
import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;

public class BasicWorld {
    public static void main(String... args) {
        World basicWorld = new WorldImpl(GJMS.worldHandler, new RegistryCodecImpl(
                new DimensionTypeRegistryEntry[]{
                        new DimensionTypeRegistryEntryImpl(
                                "gjms:stone_platform",
                                new DimensionTypeRegistryEntryElementImpl(
                                        false,
                                        false,
                                        7,
                                        7,
                                        true,
                                        0.2f,
                                        null,
                                        "#",
                                        false,
                                        true,
                                        true,
                                        "",
                                        0,
                                        256,
                                        256,
                                        1,
                                        true,
                                        false
                                ))
                })
        );

        ConnectionHandler prePlayConnectionHandler = new PrePlayConnectionHandler(new FirstWorldGetterImpl(),
                new ServerListPingStatusGetterImpl());

        NewConnectionHandler newConnectionHandler = new NewConnectionHandler(25565, true,
                prePlayConnectionHandler);

        newConnectionHandler.startNewHandler();
        prePlayConnectionHandler.startNewHandler();
    }
}

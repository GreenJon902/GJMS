package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.basicWorld.RegistryCodecImpl.RegistryCodecImpl;
import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.Player;
import com.greenjon902.gjms.common.RegistryCodec.RegistryCodec;
import com.greenjon902.gjms.common.World;
import com.greenjon902.gjms.common.WorldHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldImpl implements World {
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final ConnectionHandler connectionHandler;

    public WorldImpl(WorldHandler worldHandler, RegistryCodec registryCodec) {
        connectionHandler = new ConnectionHandlerImpl(registryCodec);

        worldHandler.register(this, "BasicWorld");
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
        connectionHandler.addConnection(player);
    }
}

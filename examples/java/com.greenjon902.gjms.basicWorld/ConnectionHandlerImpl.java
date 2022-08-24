package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.common.Connection;
import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.Player;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectionHandlerImpl implements ConnectionHandler {
    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void addConnection(@NotNull Connection connection) {
        connections.add(connection);
    }

    @Override
    public void startNewHandler() {

    }

    @Override
    public Connection[] getConnections() {
        return connections.toArray(Connection[]::new);
    }
}

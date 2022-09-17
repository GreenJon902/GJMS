package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.common.*;
import com.greenjon902.gjms.common.RegistryCodec.RegistryCodec;
import com.greenjon902.gjms.connection.play.packetAdapter.packet.clientbound.Login;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectionHandlerImpl implements ConnectionHandler {
    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    private RegistryCodec registryCodec;

    public ConnectionHandlerImpl(RegistryCodec registryCodec) {
        this.registryCodec = registryCodec;
    }

    @Override
    public void addConnection(@NotNull Connection connection) {
        connections.add(connection);

        Login loginPacket = new Login(
                0,
                false,
                Gamemode.SURVIVAL,
                Gamemode.SURVIVAL,
                new Identifier[] {new IdentifierImpl("gjms", "basic_world")},
                registryCodec,
                new IdentifierImpl("gjms", "stone_platform"),
                new IdentifierImpl("gjms", "basic_world"),
                0,
                10,
                8,
                8,
                false,
                true,
                false,
                false
        );
        try {
            connection.send(loginPacket);
        } catch (IOException e) {
            System.err.println("Failed to send Login (Play) to " + connection + "\npacket=" + loginPacket + "\n\n");
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.err.println(stackTraceElement.toString());
            }
        }
    }

    @Override
    public void startNewHandler() {

    }

    @Override
    public Connection[] getConnections() {
        return connections.toArray(Connection[]::new);
    }
}

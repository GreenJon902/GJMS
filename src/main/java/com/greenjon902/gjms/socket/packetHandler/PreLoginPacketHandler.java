package com.greenjon902.gjms.socket.packetHandler;

import com.greenjon902.gjms.socket.DataCoder;
import com.greenjon902.gjms.socket.PlayerConnection;

import java.io.IOException;

/**
 * Handles all packets that are sent before a player has logged in, this is because we have no information about the
 * player, like game version, encryption, ect.
 */
public class PreLoginPacketHandler {

    public static void processInitialConnection(PlayerConnection connection) throws IOException {
        int packetLength = DataCoder.decodeFirstVarInt(connection);
        int packetId = DataCoder.decodeFirstVarInt(connection);
    }
}

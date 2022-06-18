package com.greenjon902.gjms.socket.packetHandler;

import com.greenjon902.gjms.socket.DataCoder;
import com.greenjon902.gjms.socket.PlayerConnection;

import java.io.IOException;

/**
 * Handles all packets that are sent before a player has logged in, this is because we have no information about the
 * player, like game version, encryption, ect.
 */
public class PreLoginPacketHandler {

    /**
     * Process the first connection that is made after the client connects to the server. Will throw an error if the
     * packet id is not 0x00.
     * @param connection The connection to read the packets from
     * @throws IOException If an I/O error occurs
     */
    public static void processInitialConnection(PlayerConnection connection) throws IOException {
        int packetLength = DataCoder.decodeFirstVarInt(connection); // We do not need this
        int packetId = DataCoder.decodeFirstVarInt(connection);
        PacketType packetType = PacketType.fromId(packetId);

        if (packetType != PacketType.HANDSHAKE) {
            throw new WrongInitialPacketException("Recieved packet of type " + packetType);
        }
    }

    public static class WrongInitialPacketException extends RuntimeException {
        public WrongInitialPacketException(String message) {
            super(message);
        }
    }
}

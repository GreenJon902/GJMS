package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Any class that extends this class will be used to decode bytes in a packet from a {@link InputStream} to a
 * usable format, they can also encode a packet for sending through the network.
 */
public abstract class PacketAdapter {
    private static final int SEGMENT_BITS = 0x7F; // 11111110
    private static final int CONTINUE_BIT = 0x80; // 10000000

    /**
     * Reads the first variable length integer from the players incoming packets.
     * VarInts are stored in a way that each byte has 7 bits of data which is prefixed by a bit that shows whether
     * to continue or not, if it is a 0 then the varInt is over, if it is a 1 then the varInt has another byte of data.
     * VarInts can store up to 5 bits of data.
     *
     * @param connection The connection where the packet is coming from
     * @return The integer that was decoded
     * @throws IOException If an I/O error occurs
     */
    public static int decodeFirstVarInt(@NotNull Connection connection) throws IOException {
        int value = 0; // The number that is being decoded
        int position = 0; // The position of value that is currently being edited

        while (true) {
            byte currentByte = (byte) connection.inputStream.read();

            value |= (currentByte & SEGMENT_BITS) << position; // adds the new byte to the value
            if ((currentByte & CONTINUE_BIT) == 0) break; // is the number finished?
            position += 7; // because bytes in varInts store 7 bits of data
            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    /**
     * Reads the first string from the players incoming packets.
     * Strings are transmitted in UTF-8 and are prefixed by a varInt (see {@link #decodeFirstVarInt(Connection)}).
     *
     * @param connection The connection where the packet is coming from
     * @return The string that was decoded
     * @throws IOException If an I/O error occurs
     */
    @Contract("_ -> new")
    public static @NotNull String decodeFirstString(@NotNull Connection connection) throws IOException {
        int length = decodeFirstVarInt(connection);
        byte[] bytes = new byte[length];
        connection.inputStream.read(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static int decodeFirstUnsignedShort(@NotNull Connection connection) throws IOException {
        int value = 0;
        value += connection.inputStream.read() << 8;
        value += connection.inputStream.read();
        return value;
    }

    /**
     * Gets the usable instance of the {@link PacketAdapter}.
     *
     * @return The instance of the packet adapter
     */
    public static @NotNull PacketAdapter getInstance() {
        throw new RuntimeException("getInstance has not been implemented yet");
    }

    /**
     * Gets the first packet from a connection
     *
     * @param connection The connection where the packet is coming from
     * @return The packet that has been got
     */
    public abstract Packet getFirstPacket(Connection connection) throws IOException;
}

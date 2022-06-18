package com.greenjon902.gjms.socket;

import java.io.IOException;

/**
 * A utility class to encode and decode various data types that are sent to and from the Minecraft Client.
 */
public class DataCoder {
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
    public static int decodeFirstVarInt(PlayerConnection connection) throws IOException {
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
}

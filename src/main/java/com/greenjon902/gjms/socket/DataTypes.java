package com.greenjon902.gjms.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataTypes {
    private static final int SEGMENT_BITS = 0x7F; // 11111110
    private static final int CONTINUE_BIT = 0x80; // 10000000

    /**
     * Reads the first variable length integer from the byte array.
     * VarInts are stored in a way that each byte has 7 bits of data which is prefixed by a bit that shows whether
     * to continue or not, if it is a 0 then the varInt is over, if it is a 1 then the varInt has another byte of data.
     * VarInts can store up to 5 bits of data.
     *
     * @param bytes The byte array to read from
     * @return The integer that was created
     */
    public static int decodeFirstVarInt(byte[] bytes) {
        int value = 0; // The number that is being decoded
        int position = 0; // The position of value that is currently being edited

        for (byte currentByte : bytes) {
            value |= (currentByte & SEGMENT_BITS) << position; // adds the new byte to the value
            if ((currentByte & CONTINUE_BIT) == 0) break; // is the number finished?
            position += 7; // because bytes in varInts store 7 bits of data
            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }
}
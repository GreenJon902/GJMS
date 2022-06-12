package com.greenjon902.gjms.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataTypes {
    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static int decodeVarInt(InputStream inputStream) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = (byte) inputStream.read();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static void writeVarInt(OutputStream outputStream, int value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                outputStream.write(value);
                return;
            }

            outputStream.write((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }
}

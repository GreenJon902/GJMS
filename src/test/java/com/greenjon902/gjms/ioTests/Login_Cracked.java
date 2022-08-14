package com.greenjon902.gjms.ioTests;

import com.greenjon902.gjms.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.greenjon902.gjms.Utils.byteArray;
import static com.greenjon902.gjms.Utils.readVarInt;

public class Login_Cracked {
    private final String ip = "127.0.0.1";

    @Test
    public void MC1_19() throws IOException {
        // Setup ---
        int port = Utils.makeServer(true);
        System.out.println("Got port " + port);
        Socket socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();


        byte[] packets = byteArray(
                // Handshake ------------------------------
                // Length - VarInt
                0x10, // 00010000
                // Packet ID - VarInt
                0x00, // 00000000

                // Protocol Version - VarInt
                0xf6, // 11110110
                0x05, // 00000101
                // Server Address - String (255)
                0x09, // 00001001
                '1', '2', '7', '.', '0', '.', '0', '.', '1',
                // Server Port - Unsigned Short
                0x63, // 01100011
                0xdd, // 11011101
                // Next State - VarInt
                0x02, // 00000001

                // Login Start -------------------------
                // Length - VarInt
                0x0B, // 00001011
                // Packet ID - VarInt
                0x00, // 00000000

                // Name - String (16)
                0x08, // 00001000
                'G', 'r', 'e', 'e', 'n', 'J', 'o', 'n',
                // Has Signature Data - Boolean
                0x00 // 00000000
        );

        // Run ---
        outputStream.write(packets);

        // Check ---
        // Login Success  -

        // Packet length
        readVarInt(inputStream); // TODO: Check packet length

        // Packet ID
        Assert.assertEquals("Wrong PacketId for LoginSuccess", 0x02, inputStream.read());

        // UUID
        long mostSignificant = 0;
        for (int i=0;i<8;i++) {
            mostSignificant <<= 8;
            mostSignificant |= inputStream.read() & 0xFF;
        }
        long leastSignificant = 0;
        for (int i=0;i<8;i++) {
            leastSignificant <<= 8;
            leastSignificant |= inputStream.read() & 0xFF;
        }
        Assert.assertEquals(UUID.nameUUIDFromBytes("OfflinePlayer:GreenJon".getBytes(StandardCharsets.UTF_8)), new UUID(mostSignificant, leastSignificant));

        // Username
        int usernameLength = readVarInt(inputStream);
        byte[] usernameBytes = new byte[usernameLength];
        inputStream.read(usernameBytes);
        String username = new String(usernameBytes, StandardCharsets.UTF_8);
        Assert.assertEquals(username, "GreenJon");

        // Properties
        int numberOfProperties = readVarInt(inputStream);

        for (int i=0; i<numberOfProperties; i++) {
            int keyLength = readVarInt(inputStream);
            byte[] keyBytes = new byte[keyLength];
            inputStream.read(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8);

            int valueLength = readVarInt(inputStream);
            byte[] valueBytes = new byte[valueLength];
            inputStream.read(valueBytes);
            String value = new String(valueBytes, StandardCharsets.UTF_8);

            System.out.println("Got property \"" + key + "\" where value is \"" + value + "\"");

            boolean hasSignatureData = inputStream.read() == 1;
            if (hasSignatureData) {
                int signatureDataLength = readVarInt(inputStream);
                byte[] signatureDataBytes = new byte[signatureDataLength];
                inputStream.read(signatureDataBytes);
                String signatureData = new String(signatureDataBytes, StandardCharsets.UTF_8);

                System.out.println("Signature data is \"" + signatureData + "\"");

            } else {
                System.out.println("No signature data");
            }

        }
    }
}

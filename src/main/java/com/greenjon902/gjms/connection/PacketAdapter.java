package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound.LoginSuccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * Contains the base functionality any {@link PacketAdapter} will need. Any class that extends this class will be used to
 * convert bytes from a {@link Connection} to a {@link ServerboundPacket} and be able to convert a {@link ServerboundPacket} to bytes and send
 * it through a {@link Connection}.<br>
 * The reason for this is that a game will only need to understand one packet system,
 * but it will be able to communicate with legacy and future clients without any extra development as all conversions
 * will be handled by the {@link PacketAdapter}.
 */
public abstract class PacketAdapter {
    private static final int SEGMENT_BITS = 0x7F; // 01111111
    private static final int CONTINUE_BIT = 0x80; // 10000000
    private static final int BYTE_BITS = 0xFF; // 11111111
    private static final int BOOL_TRUE_BITS = 0x01; // 00000001

    /**
     * Reads the first variable length integer from the players incoming packets.
     * VarInts are stored in a way that each byte has 7 bits of data which is prefixed by a bit that shows whether
     * to continue or not, if it is a 0 then the varInt is over, if it is a 1 then the varInt has another byte of data.
     * VarInts can store up to 5 bytes of data.
     *
     * @param inputStream The stream varInt is coming from
     * @return The integer that was decoded
     * @throws IOException If an I/O error occurs
     */
    public int decodeFirstVarInt(@NotNull InputStream inputStream) throws IOException {
        int value = 0; // The number that is being decoded
        int position = 0; // The position of value that is currently being edited

        while (true) {
            byte currentByte = (byte) inputStream.read();

            value |= (currentByte & SEGMENT_BITS) << position; // adds the new byte to the value

            if ((currentByte & CONTINUE_BIT) == 0) break; // is the number finished?
            position += 7; // because bytes in varInts store 7 bits of data
            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    /**
     * Reads the first boolean from the connection.
     *
     * @param inputStream The stream boolean is coming from
     * @return The boolean that was decoded
     * @throws IOException If an I/O error occurs
     */
    public boolean decodeFirstBoolean(@NotNull InputStream inputStream) throws IOException {
        return (inputStream.read() & BOOL_TRUE_BITS) == 1;
    }

    /**
     * Reads the byte array from the connection, it will also read the first varInt as that signifies the length of the
     * array.
     *
     * @param inputStream The stream where the byte array is coming from
     * @return The byte[] that was decoded
     * @throws IOException If an I/O error occurs
     */
    public byte[] decodeFirstByteArray(@NotNull InputStream inputStream) throws IOException {
        int length = decodeFirstVarInt(inputStream);
        byte[] content = new byte[length];
        inputStream.read(content);
        return content;
    }

    /**
     * Encodes an integer as a varInt in a byte array.
     * VarInts are stored in a way that each byte has 7 bits of data which is prefixed by a bit that shows whether
     * to continue or not, if it is a 0 then the varInt is over, if it is a 1 then the varInt has another byte of data.
     * VarInts can store up to 5 bytes of data.
     *
     * @param value The int to be encoded
     * @return The byte[] of the varInt
     */
    public byte[] encodeVarInt(int value) {
        byte[] encoded = new byte[5]; // maximum it can hold
        int i = 0;
        while (true){
            if ((value & ~SEGMENT_BITS) == 0) {
                encoded[i] = (byte) value;
                break;
            }
            encoded[i] = (byte) ((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
            i++;
        }

        byte[] croppedEncoded = new byte[i+1];
        System.arraycopy(encoded, 0, croppedEncoded, 0, i+1); // remove the empty values from the array
        return croppedEncoded;
    }

    /**
     * Reads the first string from the players incoming packets.
     * Strings are transmitted in UTF-8 and are prefixed by a varInt (see {@link #decodeFirstVarInt(InputStream)}).
     *
     * @param inputStream The connection where the string is coming from
     * @return The string that was decoded
     * @throws IOException If an I/O error occurs
     */
    @Contract("_ -> new")
    public @NotNull String decodeFirstString(@NotNull InputStream inputStream) throws IOException {
        int length = decodeFirstVarInt(inputStream);
        byte[] bytes = new byte[length];
        inputStream.read(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Gets the first {@link Short} from a connection.
     *
     * @param inputStream The short where the packet is coming from
     * @return The long that was decoded
     * @throws IOException If an I/O error occurs
     */
    public int decodeFirstUnsignedShort(@NotNull InputStream inputStream) throws IOException {
        int value = 0;
        value += inputStream.read() << 8;
        value += inputStream.read();
        return value;
    }

    /**
     * Gets the first {@link Long} from a connection.
     *
     * @param inputStream The stream where the long is coming from
     * @return The long that was decoded
     * @throws IOException If an I/O error occurs
     */
    public long decodeFirstLong(@NotNull InputStream inputStream) throws IOException {
        long value = 0;
        for (int i=0;i<8;i++) {
            value <<= 8;
            value |= inputStream.read() & BYTE_BITS;
        }
        return value;
    }

    /**
     * Encodes a long by turning it into bytes.
     *
     * @param value The long to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeLong(long value) {
        return new byte[] {
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    /**
     * Encodes a byte[] by prefixing it with its length.
     *
     * @param byteArray The bytes to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeBytes(byte[] byteArray) {
        byte[] length = encodeVarInt(byteArray.length);
        byte[] bytes = new byte[byteArray.length + length.length];
        System.arraycopy(length, 0, bytes, 0, length.length);
        System.arraycopy(byteArray, 0, bytes, length.length, byteArray.length);
        return bytes;
    }

    /**
     * Encodes a string by turning it into bytes and prefixing it with its length.
     *
     * @param string The string to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeString(String string) {
        return encodeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Encodes a boolean by  turning it into bytes.
     *
     * @param value The boolean to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeBoolean(boolean value) {
        return new byte[] {(byte) (value ? 0x01 : 0x00)};
    }

    /**
     * Encodes a UUID by turning it into 16 bytes / two unsigned longs.
     *
     * @param uuid The uuid to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeUUID(UUID uuid) {
        byte[] mostSignificant = encodeLong(uuid.getMostSignificantBits());
        byte[] leastSignificant = encodeLong(uuid.getLeastSignificantBits());

        byte[] bytes = new byte[16];
        System.arraycopy(mostSignificant, 0, bytes, 0, 8);
        System.arraycopy(leastSignificant, 0, bytes, 8, 8);

        return bytes;
    }

    /**
     * Encodes a {@link LoginSuccess.Property}.
     *
     * @param property The property to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeProperty(LoginSuccess.Property property) {
        byte[] name = encodeString(property.name);
        byte[] value = encodeString(property.value);
        byte[] isSigned = encodeBoolean(property.isSigned());
        byte[] signature; // 0 length if not signed

        if (property.isSigned()) {
            signature = encodeString(property.signature);
        } else {
            signature = new byte[0];
        }

        byte[] bytes = new byte[name.length + value.length + isSigned.length + signature.length];
        System.arraycopy(name, 0, bytes, 0, name.length);
        System.arraycopy(value, 0, bytes, name.length, value.length);
        System.arraycopy(isSigned, 0, bytes, name.length + value.length, isSigned.length);
        System.arraycopy(signature, 0, bytes, name.length + value.length + isSigned.length, signature.length);

        return bytes;
    }

    /**
     * Encodes a {@link LoginSuccess.Property} array.
     *
     * @param properties The properties to be encoded
     * @return The bytes that were got
     */
    public byte[] encodePropertyArray(LoginSuccess.Property[] properties) {
        // We don't know what the full length is yet, so we can't make the final array
        byte[][] encodedIndividualProperties = new byte[properties.length][];
        int totalLength = 0;
        for (int i=0; i < properties.length; i++) {
            byte[] encodedProperty = encodeProperty(properties[i]);
            encodedIndividualProperties[i] = encodedProperty;
            totalLength += encodedProperty.length;
        }

        byte[] encodedTotalLength = encodeVarInt(totalLength);
        byte[] encodedProperties = new byte[encodedTotalLength.length + totalLength];
        System.arraycopy(encodedTotalLength, 0, encodedProperties, 0, encodedTotalLength.length);

        int offset = encodedTotalLength.length;
        for (byte[] encodedProperty : encodedIndividualProperties) {
            System.arraycopy(encodedProperty, 0, encodedProperties, offset, encodedProperty.length);
            offset += encodedProperty.length;
        }

        return encodedProperties;
    }

    /**
     * Reads the next packet bytes into a byte[], see {@link #decodeFirstByteArray(InputStream)}
     *
     * @param connection The connection where the packet is coming from
     * @return The packet that has been got
     * @throws IOException If an I/O error occurs
     */
    public byte[] readNextPacketFrom(Connection connection) throws IOException {
        return decodeFirstByteArray(connection.inputStream);
    }

    /**
     * Decodes the first packet from an input stream, the stream should only contain one packet - use
     * {@link #readNextPacketFrom(Connection)}.
     *
     * @param inputStream The stream where the packet is coming from
     * @return The packet that was decoded
     * @throws IOException If an I/O error occurs
     */
    public abstract ServerboundPacket decodePacket(InputStream inputStream) throws IOException;

    /**
     * Encodes a {@link ClientboundPacket} to a byte[].
     *
     * @param clientboundPacket The packet to be encoded
     * @return The encoded packet as a byte[]
     */
    public abstract byte[] encodePacket(ClientboundPacket clientboundPacket);
}

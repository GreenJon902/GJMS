package com.greenjon902.gjms.socket.packetHandler;

/**
 * This stores PacketTypes as an {@link Enum}, rather than an {@link Integer}, this provides explicitness, so it cannot be
 * accidentally used or confused for something else.
 */
public enum PacketType {
    HANDSHAKE; // TODO: Add rest of packet types

    /**
     * Used instead of calling {@link PacketType#getIdToPacketTypeContents()} each time.
     */
    private static final PacketType[] idToPacketType = PacketType.getIdToPacketTypeContents();

    /**
     * Generates an array which converts packet ids to packetType
     * @return The array
     */
    private static PacketType[] getIdToPacketTypeContents() {
        PacketType[] idToPacketType = new PacketType[0xFF]; // That is the highest amount of packets probably

        idToPacketType[0x00] = PacketType.HANDSHAKE;

        return idToPacketType;
    }

    /**
     * Converts packetIds to PacketTypes.
     *
     * @param id The id of the packet type
     * @return The type of packet that <code>Id</code> represents
     */
    public static PacketType fromId(int id) {
        return idToPacketType[id];
    }
}

package com.greenjon902.gjms.connection;

public interface Packet {
    /**
     * The packet id is the way that minecraft clients are able to understand what each packet is and what it will do.
     *
     * @return The packet id of this packet
     */
    int getPacketId();
}

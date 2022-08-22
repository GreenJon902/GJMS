package com.greenjon902.gjms.connection;

/**
 * A packet is a piece of information sent between the minecraft client and server, all packets on GJMS should be of the
 * latest packet specifications, if an older client connects then a packet adaptor should modify the information, for
 * modern clients the packet adaptor just reads the bytes into a Packet class and writes a Packet class into bytes.
 */
public abstract class Packet {
    /**
     * The packet id is the way that minecraft clients are able to understand what each packet is and what it will do.
     *
     * @return The packet id of this packet
     */
    public abstract int getPacketId();
}

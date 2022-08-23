package com.greenjon902.gjms.common;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.ServerboundPacket;

import java.io.IOException;

public interface Connection {
    /**
     * Send a packet to the other end of this connection.
     *
     * @param clientboundPacket The packet to be sent
     * @throws IOException If an I/O error occurs
     */
    void send(ClientboundPacket clientboundPacket) throws IOException;

    /**
     * Read the next packet from this connection.
     *
     * @return The packet that was read
     * @throws IOException If an I/O error occurs
     */
    ServerboundPacket receive() throws IOException;
}

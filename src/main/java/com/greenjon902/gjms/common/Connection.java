package com.greenjon902.gjms.common;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.ServerboundPacket;
import org.jetbrains.annotations.Nullable;

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
     * Returns null if no packet available
     *
     * @return The packet that was read
     * @throws IOException If an I/O error occurs
     */
    @Nullable ServerboundPacket receive() throws IOException;

    /**
     * Gets the name of the source of this connection!
     * NOTE: This should only be used for logging!
     *
     * @return The name of the source
     */
    String getConnectionSourceName();
}

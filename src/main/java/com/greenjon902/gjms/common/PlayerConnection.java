package com.greenjon902.gjms.common;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.ServerboundPacket;

import java.io.IOException;

public interface PlayerConnection {
    void send(ClientboundPacket clientboundPacket) throws IOException;
    ServerboundPacket receive() throws IOException;
}

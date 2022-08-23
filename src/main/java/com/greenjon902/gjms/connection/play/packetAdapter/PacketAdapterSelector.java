package com.greenjon902.gjms.connection.play.packetAdapter;

import com.greenjon902.gjms.connection.PacketAdapter;

import java.util.HashMap;

public class PacketAdapterSelector {
    private static final HashMap<Integer, PacketAdapter> packetAdapters = new HashMap<>();

    /**
     * Selects the correct {@link PacketAdapter} instance for the arguments supplied.
     *
     * @param protocolVersion The connection's protocol version
     * @return The selected {@link PacketAdapter}
     */
    public static PacketAdapter selectAdapter(int protocolVersion) {
        return packetAdapters.computeIfAbsent(protocolVersion, PacketAdapterSelector::createAdapter);
    }

    /**
     * Creates a new {@link com.greenjon902.gjms.connection.PacketAdapter} for the arguments supplied.
     * @param protocolVersion The connection's protocol version
     * @return The created {@link PacketAdapter}
     */
    private static PacketAdapter createAdapter(int protocolVersion) {
        return switch (protocolVersion) {
            case 760 -> new PacketAdapter760(); // 1.19.1, 1.19.2
            case 47 -> new PacketAdapter47();   // 1.8, 1.8.1, 1.8.2, 1.8.3, 1.8.4, 1.8.5, 1.8.6, 1.8.7, 1.8.8, 1.8.9
            default -> throw new IllegalArgumentException("PacketAdapterSelector#createAdapter cannot create adapters for protocol version " + protocolVersion);
        };
    }
}

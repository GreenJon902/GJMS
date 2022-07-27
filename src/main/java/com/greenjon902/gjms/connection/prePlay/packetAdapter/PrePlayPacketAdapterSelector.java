package com.greenjon902.gjms.connection.prePlay.packetAdapter;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.HandshakePacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.StatusPacketAdapter0to759;

import java.util.HashMap;

/**
 * A helper class to get the correct {@link PrePlayPacketAdapter} for
 * {@link com.greenjon902.gjms.connection.prePlay.PrePlayConnection}s.
 */
public class PrePlayPacketAdapterSelector {
    private static final HashMap<PrePlayConnectionState, HashMap<Integer, PrePlayPacketAdapter>> packetAdapters = new HashMap<>();
    static {
        for (PrePlayConnectionState prePlayConnectionState : PrePlayConnectionState.values()) {
            packetAdapters.put(prePlayConnectionState, new HashMap<>());
        }
    }

    /**
     * Selects the correct {@link PrePlayPacketAdapter} instance for the arguments supplied.
     *
     * @param state The {@link PrePlayConnectionState} of the connection
     * @param protocolVersion The connection's protocol version
     * @return The selected {@link PrePlayPacketAdapter}
     */
    public static PrePlayPacketAdapter selectAdapter(PrePlayConnectionState state, int protocolVersion) {
        return packetAdapters.get(state).computeIfAbsent(protocolVersion, v -> createAdapter(state, v));
    }

    /**
     * Creates a new {@link PrePlayPacketAdapter} for the arguments supplied.
     * @param state The {@link PrePlayConnectionState} of the connection
     * @param protocolVersion The connection's protocol version
     * @return The created {@link PrePlayPacketAdapter}
     */
    private static PrePlayPacketAdapter createAdapter(PrePlayConnectionState state, int protocolVersion) {
        switch (state) {
            case HANDSHAKE -> {
                return new HandshakePacketAdapter(); // We shouldn't have a protocol version yet
            }
            case STATUS -> {
                return new StatusPacketAdapter0to759(); // Works the same for all of these versions
            }
            default -> throw new RuntimeException("Create adaptor only has HandshakePacketAdaptors and StatusPacketAdapters implemented");
        }
    }
}

package com.greenjon902.gjms.connection.prePlay.packetAdapter;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.HandshakePacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.LoginPacketAdapter391to760;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.StatusPacketAdapter0to760;

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
                return new StatusPacketAdapter0to760(); // Works the same for all of these versions
            }
            case LOGIN -> {
                if (391 <= protocolVersion && protocolVersion <= 760) {
                    return new LoginPacketAdapter391to760();
                } else {
                    throw new RuntimeException("LoginPacketAdaptors only support protocol versions between 391 and 760");
                }
            }
            default -> throw new RuntimeException("Create adaptor only has HandshakePacketAdaptors, " +
                    "StatusPacketAdapters, and LoginPacketAdaptors implemented"); // These should be the only ones in
                                                                                  // the switch
        }
    }
}

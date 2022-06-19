package com.greenjon902.gjms.connection;

/**
 * The state that a {@link PlayerConnection} can currently be in.
 */
public enum ConnectionState {
    HANDSHAKE, STATUS, LOGIN, PLAY
}

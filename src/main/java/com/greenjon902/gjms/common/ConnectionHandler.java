package com.greenjon902.gjms.common;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Handles incoming packets from {@link PlayerConnection}.
 */
public interface ConnectionHandler {
    /**
     * Adds a connection that will need to be handled.
     *
     * @param playerConnection The connection to be added
     */
    void addConnection(@NotNull PlayerConnection playerConnection);

    /**
     * Starts a new thread, these constantly loop through the current open connections and handle the packets. You can
     * have multiple running at one time to improve speed when there is a large number of connections.
     */
    void startNewHandler();

    /**
     * Gets all open {@link PlayerConnection}s for this handler.
     *
     * @return All open connections
     */
    PrePlayConnection[] getConnections();
}

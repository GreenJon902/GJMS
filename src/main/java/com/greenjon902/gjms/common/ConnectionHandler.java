package com.greenjon902.gjms.common;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Handles incoming packets from {@link Connection}.
 */
public interface ConnectionHandler {
    /**
     * Adds a connection that will need to be handled.
     *
     * @param connection The connection to be added
     */
    void addConnection(@NotNull Connection connection);

    /**
     * Starts a new thread, these constantly loop through the current open connections and handle the packets. You can
     * have multiple running at one time to improve speed when there is a large number of connections.
     */
    void startNewHandler();

    /**
     * Gets all open {@link Connection}s for this handler.
     *
     * @return All open connections
     */
    Connection[] getConnections();
}

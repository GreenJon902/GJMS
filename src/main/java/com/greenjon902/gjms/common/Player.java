package com.greenjon902.gjms.common;

public interface Player {
    /**
     * Get the connection to the player's client.
     *
     * @return The connection
     */
    PlayerConnection getConnection();
}

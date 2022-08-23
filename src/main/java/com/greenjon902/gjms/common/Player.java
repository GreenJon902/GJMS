package com.greenjon902.gjms.common;

import java.util.UUID;

public interface Player extends Connection {
    /**
     * Gets this player's unique id, this should be used as a key when accessing data specific to this player.
     *
     * @return The playerId
     */
    UUID getPlayerId();

    /**
     * Gets this players name, this may not be unique so should not be used for anything but chat and console output.
     *
     * @return This player's name
     */
    String getName();
}

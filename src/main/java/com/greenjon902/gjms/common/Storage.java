package com.greenjon902.gjms.common;

/**
 * A class to get and set information about the server and it's players.
 */
public interface Storage {
    /**
     * Gets the last world the player supplied was in, if the player has no last world then the default world should be
     * returned.
     *
     * @param player The player to get the world of
     * @return The id of the world
     */
    String getLastWorldOf(Player player);
}

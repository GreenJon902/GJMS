package com.greenjon902.gjms.common;

public interface World {
    /**
     * Add a player to a world.
     * Note: This should not handle any disconnection from the last world / connection handler, if that is required then
     * it should be done before this is called.
     *
     * @param player The player to be added
     */
    void addPlayer(Player player);
}

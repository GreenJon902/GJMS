package com.greenjon902.gjms.common;

public interface World {
    /**
     * Add a player to a world.
     *
     * @param playerConnection The player and it's connection
     */
    void addPlayer(PlayerConnection playerConnection);
}

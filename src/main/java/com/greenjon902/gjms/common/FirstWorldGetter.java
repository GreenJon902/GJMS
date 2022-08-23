package com.greenjon902.gjms.common;

import java.util.UUID;

/**
 * A class to get the world of the player after they first join.
 */
public interface FirstWorldGetter {
    /**
     * Gets the world the player will go too after they first join.
     *
     * @param playerId The player that just joined
     * @return the world id
     */
    String getWorld(UUID playerId);
}

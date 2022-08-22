package com.greenjon902.gjms.common;

/**
 * A class that stores all the {@link World}s.
 */
public interface WorldHandler {
    /**
     * Register a world and its id.
     *
     * @param world The world that is being registered
     * @param id The id of that world
     */
    void register(World world, String id);

    /**
     * Unregister a world and its id.
     *
     * @param id The id of that world
     */
    void unregister(String id);

    /**
     * Get a world by its id.
     *
     * @param id The id of the world
     * @return The world with the id given
     */
    World get(String id);
}

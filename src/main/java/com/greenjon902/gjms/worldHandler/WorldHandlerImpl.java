package com.greenjon902.gjms.worldHandler;

import com.greenjon902.gjms.common.World;
import com.greenjon902.gjms.common.WorldHandler;

import java.util.HashMap;

public class WorldHandlerImpl implements WorldHandler {
    private final HashMap<String, World> worlds = new HashMap<>();

    @Override
    public void register(World world, String id) throws IllegalArgumentException {
        if (worlds.containsKey(id)) throw new IllegalArgumentException("World with id " + id + " already exists!");
        worlds.put(id, world);
    }

    @Override
    public void unregister(String id) {
        worlds.remove(id);
    }

    @Override
    public World get(String id) throws IllegalArgumentException {
        if (!worlds.containsKey(id)) throw new IllegalArgumentException("World with id " + id + " doesn't exists!");
        return worlds.get(id);
    }
}

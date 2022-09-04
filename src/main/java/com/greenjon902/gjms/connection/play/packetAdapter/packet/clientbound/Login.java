package com.greenjon902.gjms.connection.play.packetAdapter.packet.clientbound;

import com.greenjon902.gjms.common.Gamemode;
import com.greenjon902.gjms.common.Location;
import com.greenjon902.gjms.common.RegistryCodec.RegistryCodec;

public class Login {
    public final int entityId;
    public final boolean isHardcore;
    public final Gamemode previousGamemode;
    public final Gamemode gamemode;
    public final String[] dimensionNames;
    public final RegistryCodec registryCodec;
    public final int dimensionTypeIdentifier;
    public final int dimensionNameIdentifier;
    public final long hashedSee;
    public final int maxPlayers;
    public final int viewDistance;
    public final int simulationDistance;
    public final boolean reducedDebugInfo;
    public final boolean enableRespawnScreen;
    public final boolean isDebug;
    public final boolean isFlat;
    public final boolean deathDimensionNameIdentifier;
    public final Location deathLocation;

    public Login(int entityId, boolean isHardcore, Gamemode previousGamemode, Gamemode gamemode, String[] dimensionNames, RegistryCodec registryCodec, int dimensionTypeIdentifier,
                 int dimensionNameIdentifier, long hashedSee, int maxPlayers, int viewDistance, int simulationDistance,
                 boolean reducedDebugInfo, boolean enableRespawnScreen, boolean isDebug, boolean isFlat,
                 boolean deathDimensionNameIdentifier, Location deathLocation) {
        this.entityId = entityId;
        this.isHardcore = isHardcore;
        this.previousGamemode = previousGamemode;
        this.gamemode = gamemode;
        this.dimensionNames = dimensionNames;
        this.registryCodec = registryCodec;
        this.dimensionTypeIdentifier = dimensionTypeIdentifier;
        this.dimensionNameIdentifier = dimensionNameIdentifier;
        this.hashedSee = hashedSee;
        this.maxPlayers = maxPlayers;
        this.viewDistance = viewDistance;
        this.simulationDistance = simulationDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.enableRespawnScreen = enableRespawnScreen;
        this.isDebug = isDebug;
        this.isFlat = isFlat;
        this.deathDimensionNameIdentifier = deathDimensionNameIdentifier;
        this.deathLocation = deathLocation;
    }
}

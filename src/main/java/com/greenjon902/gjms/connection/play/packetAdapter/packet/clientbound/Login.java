package com.greenjon902.gjms.connection.play.packetAdapter.packet.clientbound;

import com.greenjon902.gjms.common.Gamemode;
import com.greenjon902.gjms.common.Identifier;
import com.greenjon902.gjms.common.Location;
import com.greenjon902.gjms.common.RegistryCodec.RegistryCodec;
import com.greenjon902.gjms.connection.ClientboundPacket;

import java.util.Arrays;

public class Login extends ClientboundPacket {
    public final int entityId;
    public final boolean isHardcore;
    public final Gamemode previousGamemode;
    public final Gamemode gamemode;
    public final Identifier[] dimensionNames;
    public final RegistryCodec registryCodec;
    public final Identifier dimensionType;
    public final Identifier dimensionName;
    public final long hashedSeed;
    public final int maxPlayers;
    public final int viewDistance;
    public final int simulationDistance;
    public final boolean reducedDebugInfo;
    public final boolean enableRespawnScreen;
    public final boolean isDebug;
    public final boolean isFlat;
    public final Identifier deathDimensionName;
    public final Location deathLocation;

    public Login(int entityId, boolean isHardcore, Gamemode previousGamemode, Gamemode gamemode,
                 Identifier[] dimensionNames, RegistryCodec registryCodec, Identifier dimensionType,
                 Identifier dimensionName, long hashedSeed, int maxPlayers, int viewDistance, int simulationDistance,
                 boolean reducedDebugInfo, boolean enableRespawnScreen, boolean isDebug, boolean isFlat) {
        this(entityId, isHardcore, previousGamemode, gamemode, dimensionNames, registryCodec, dimensionType,
                dimensionName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, isDebug, isFlat, null, null);
    }

    public Login(int entityId, boolean isHardcore, Gamemode previousGamemode, Gamemode gamemode,
                 Identifier[] dimensionNames, RegistryCodec registryCodec, Identifier dimensionType,
                 Identifier dimensionName, long hashedSeed, int maxPlayers, int viewDistance, int simulationDistance,
                 boolean reducedDebugInfo, boolean enableRespawnScreen, boolean isDebug, boolean isFlat,
                 Identifier deathDimensionName, Location deathLocation) {
        this.entityId = entityId;
        this.isHardcore = isHardcore;
        this.previousGamemode = previousGamemode;
        this.gamemode = gamemode;
        this.dimensionNames = dimensionNames;
        this.registryCodec = registryCodec;
        this.dimensionType = dimensionType;
        this.dimensionName = dimensionName;
        this.hashedSeed = hashedSeed;
        this.maxPlayers = maxPlayers;
        this.viewDistance = viewDistance;
        this.simulationDistance = simulationDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.enableRespawnScreen = enableRespawnScreen;
        this.isDebug = isDebug;
        this.isFlat = isFlat;
        this.deathDimensionName = deathDimensionName;
        this.deathLocation = deathLocation;
    }

    @Override
    public int getPacketId() {
        return 0x25;
    }

    @Override
    public String toString() {
        return "Login{" +
                "entityId=" + entityId +
                ", isHardcore=" + isHardcore +
                ", previousGamemode=" + previousGamemode +
                ", gamemode=" + gamemode +
                ", dimensionNames=" + Arrays.toString(dimensionNames) +
                ", registryCodec=" + registryCodec +
                ", dimensionType=" + dimensionType +
                ", dimensionName=" + dimensionName +
                ", hashedSeed=" + hashedSeed +
                ", maxPlayers=" + maxPlayers +
                ", viewDistance=" + viewDistance +
                ", simulationDistance=" + simulationDistance +
                ", reducedDebugInfo=" + reducedDebugInfo +
                ", enableRespawnScreen=" + enableRespawnScreen +
                ", isDebug=" + isDebug +
                ", isFlat=" + isFlat +
                ", deathDimensionName=" + deathDimensionName +
                ", deathLocation=" + deathLocation +
                '}';
    }

    /**
     * Was a {@link #deathDimensionName} and {@link #deathLocation} specified.
     *
     * @return Whether there is a death location
     */
    public boolean hasDeathLocation() {
        return deathDimensionName != null;
    }
}

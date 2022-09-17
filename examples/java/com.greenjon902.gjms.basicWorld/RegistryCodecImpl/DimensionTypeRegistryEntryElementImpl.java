package com.greenjon902.gjms.basicWorld.RegistryCodecImpl;

import com.greenjon902.gjms.common.RegistryCodec.DimensionTypeRegistryEntryElement;
import org.jetbrains.annotations.Nullable;

public class DimensionTypeRegistryEntryElementImpl implements DimensionTypeRegistryEntryElement {
    private final boolean isPiglinSafe;
    private final boolean hasRaids;
    private final int getMonsterSpawnLightLevel; // TODO: support also TagCompound(max_inclusive (TAG_Int), min_inclusive (TAG_Int))
    private final int getMonsterSpawnBlockLightLimit;
    private final boolean isNatural;
    private final float ambientLight;
    private final @Nullable Long getFixedTime;
    private final String getInfiniteBurnTag;
    private final boolean doRespawnAnchorsWork;
    private final boolean hasSkylight;
    private final boolean bedWorks;
    private final String getEffects;
    private final int getMinY;
    private final int getWorldHeight;
    private final int getLogicalHeight; // The maximum height to which chorus fruits and nether portals can bring players
                                        // within this dimension.
    private final double getCoordinateScale; // The multiplier applied to coordinates when traveling to the dimension.
    private final boolean isUltrawarm;
    private final boolean hasCeiling;

    public DimensionTypeRegistryEntryElementImpl(boolean isPiglinSafe, boolean hasRaids, int getMonsterSpawnLightLevel, int getMonsterSpawnBlockLightLimit, boolean isNatural, float ambientLight, @Nullable Long getFixedTime, String getInfiniteBurnTag, boolean doRespawnAnchorsWork, boolean hasSkylight, boolean bedWorks, String getEffects, int getMinY, int getWorldHeight, int getLogicalHeight, double getCoordinateScale, boolean isUltrawarm, boolean hasCeiling) {
        this.isPiglinSafe = isPiglinSafe;
        this.hasRaids = hasRaids;
        this.getMonsterSpawnLightLevel = getMonsterSpawnLightLevel;
        this.getMonsterSpawnBlockLightLimit = getMonsterSpawnBlockLightLimit;
        this.isNatural = isNatural;
        this.ambientLight = ambientLight;
        this.getFixedTime = getFixedTime;
        this.getInfiniteBurnTag = getInfiniteBurnTag;
        this.doRespawnAnchorsWork = doRespawnAnchorsWork;
        this.hasSkylight = hasSkylight;
        this.bedWorks = bedWorks;
        this.getEffects = getEffects;
        this.getMinY = getMinY;
        this.getWorldHeight = getWorldHeight;
        this.getLogicalHeight = getLogicalHeight;
        this.getCoordinateScale = getCoordinateScale;
        this.isUltrawarm = isUltrawarm;
        this.hasCeiling = hasCeiling;
    }

    @Override
    public boolean isPiglinSafe() {
        return isPiglinSafe;
    }

    @Override
    public boolean hasRaids() {
        return hasRaids;
    }

    @Override
    public int getMonsterSpawnLightLevel() {
        return getMonsterSpawnLightLevel;
    }

    @Override
    public int getMonsterSpawnBlockLightLimit() {
        return getMonsterSpawnBlockLightLimit;
    }

    @Override
    public boolean isNatural() {
        return isNatural;
    }

    @Override
    public float ambientLight() {
        return ambientLight;
    }

    @Override
    public @Nullable Long getFixedTime() {
        return getFixedTime;
    }

    @Override
    public String getInfiniteBurnTag() {
        return getInfiniteBurnTag;
    }

    @Override
    public boolean doRespawnAnchorsWork() {
        return doRespawnAnchorsWork;
    }

    @Override
    public boolean hasSkylight() {
        return hasSkylight;
    }

    @Override
    public boolean bedWorks() {
        return bedWorks;
    }

    @Override
    public String getEffects() {
        return getEffects;
    }

    @Override
    public int getMinY() {
        return getMinY;
    }

    @Override
    public int getWorldHeight() {
        return getWorldHeight;
    }

    @Override
    public int getLogicalHeight() {
        return getLogicalHeight;
    }

    @Override
    public double getCoordinateScale() {
        return getCoordinateScale;
    }

    @Override
    public boolean isUltrawarm() {
        return isUltrawarm;
    }

    @Override
    public boolean hasCeiling() {
        return hasCeiling;
    }
}

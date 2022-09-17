package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface DimensionTypeRegistryEntryElement {
    boolean isPiglinSafe();
    boolean hasRaids();
    int getMonsterSpawnLightLevel(); // TODO: support also TagCompound(max_inclusive (TAG_Int), min_inclusive (TAG_Int))
    int getMonsterSpawnBlockLightLimit();
    boolean isNatural();
    float hasAmbientLight();
    @Nullable Long getFixedTime();
    String getInfiniteBurnTag();
    boolean doRespawnAnchorsWork();
    boolean hasSkylight();
    boolean bedWorks();
    String getEffects();
    int getMinY();
    int getWorldHeight();
    int getLogicalHeight(); // The maximum height to which chorus fruits and nether portals can bring players within
                            // this dimension.
    double getCoordinateScale(); // The multiplier applied to coordinates when traveling to the dimension.
    boolean isUltrawarm();
    boolean hasCeiling();

    default boolean hasFixedTime() {
        return getFixedTime() != null;
    }
}

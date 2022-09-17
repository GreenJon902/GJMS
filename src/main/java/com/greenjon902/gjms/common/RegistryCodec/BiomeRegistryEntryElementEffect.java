package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface BiomeRegistryEntryElementEffect {
    int getSkyColor();
    int getWaterFogColor();
    int getFogColor();
    int getWaterColor();
    @Nullable Integer getFoliageColor();
    @Nullable Integer getGrassColor();
    @Nullable String getGrassColorModifier();
    @Nullable BiomeRegistryEntryElementEffectMusic getMusic();
    @Nullable String getAmbientSound();
    @Nullable BiomeRegistryEntryElementEffectAdditionsSound getAdditionsSound();
    @Nullable BiomeRegistryEntryElementEffectMoodSound getMoodSound();
    @Nullable BiomeRegistryEntryElementEffectParticle getParticle();

    default boolean hasFoliageColor() {
        return getFoliageColor() != null;
    }

    default boolean hasGrassColor() {
        return getGrassColor() != null;
    }

    default boolean hasGrassColorModifier() {
        return getGrassColorModifier() != null;
    }

    default boolean hasMusic() {
        return getMusic() != null;
    }

    default boolean hasAmbientSound() {
        return getAmbientSound() != null;
    }

    default boolean hasAdditionsSound() {
        return getAdditionsSound() != null;
    }

    default boolean hasMoodSound() {
        return getMoodSound() != null;
    }

    default boolean hasParticle() {
        return getParticle() != null;
    }
}

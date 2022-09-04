package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface BiomeRegistryEntryElementEffect {
    int getSkyColor();
    int getWaterFogColor();
    int getFogColor();
    int getWaterColor();
    @Nullable Integer getFoliageColor();
    @Nullable Integer getGrassColor();

    @Nullable String getAmbientSound();


    @Nullable BiomeRegistryEntryElementEffectParticle getParticle();
}

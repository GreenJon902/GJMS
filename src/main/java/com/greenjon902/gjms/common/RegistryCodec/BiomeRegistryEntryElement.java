package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface BiomeRegistryEntryElement {
    String getPrecipitationType();
    float getDepthFactor();
    float getTemperature();
    float getDownfall();
    @Nullable String getCategory();
    @Nullable String getTemperatureModifier();
    BiomeRegistryEntryElementEffect[] getEffects();
}

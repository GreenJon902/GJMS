package com.greenjon902.gjms.common.RegistryCodec;

import com.greenjon902.gjms.common.PrecipitationType;
import org.jetbrains.annotations.Nullable;

public interface BiomeRegistryEntryElement {
    PrecipitationType getPrecipitationType();
    @Nullable Float getDepthFactor();
    float getTemperature();
    @Nullable Float getScale();
    float getDownfall();
    @Nullable String getCategory();
    @Nullable String getTemperatureModifier();
    BiomeRegistryEntryElementEffect[] getEffects();

    default boolean hasDepthFactor() {
        return getDepthFactor() != null;
    };

    default boolean hasScale() {
        return getScale() != null;
    };

    default boolean hasCategory() {
        return getCategory() != null;
    };

    default boolean hasTemperatureModifier() {
        return getTemperatureModifier() != null;
    };
}

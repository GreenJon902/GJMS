package com.greenjon902.gjms.basicWorld.RegistryCodecImpl;

import com.greenjon902.gjms.common.RegistryCodec.BiomeRegistryEntry;
import com.greenjon902.gjms.common.RegistryCodec.ChatRegistryEntry;
import com.greenjon902.gjms.common.RegistryCodec.DimensionTypeRegistryEntry;
import com.greenjon902.gjms.common.RegistryCodec.RegistryCodec;

public class RegistryCodecImpl implements RegistryCodec {
    private final DimensionTypeRegistryEntry[] dimensionTypes;

    public RegistryCodecImpl(DimensionTypeRegistryEntry[] dimensionTypes) {
        this.dimensionTypes = dimensionTypes;
    }

    @Override
    public DimensionTypeRegistryEntry[] getDimensionTypes() {
        return dimensionTypes;
    }

    @Override
    public BiomeRegistryEntry[] getBiomes() {
        return new BiomeRegistryEntry[0];
    }

    @Override
    public ChatRegistryEntry[] getChatTypes() {
        return new ChatRegistryEntry[0];
    }
}

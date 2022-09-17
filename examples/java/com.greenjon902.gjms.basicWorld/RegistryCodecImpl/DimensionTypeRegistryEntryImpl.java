package com.greenjon902.gjms.basicWorld.RegistryCodecImpl;

import com.greenjon902.gjms.common.RegistryCodec.DimensionTypeRegistryEntry;
import com.greenjon902.gjms.common.RegistryCodec.DimensionTypeRegistryEntryElement;

public class DimensionTypeRegistryEntryImpl implements DimensionTypeRegistryEntry {
    private final String name;
    private final DimensionTypeRegistryEntryElement element;

    public DimensionTypeRegistryEntryImpl(String name, DimensionTypeRegistryEntryElement element) {
        this.name = name;
        this.element = element;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DimensionTypeRegistryEntryElement getElement() {
        return element;
    }
}

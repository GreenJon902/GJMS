package com.greenjon902.gjms.common.RegistryCodec;

public interface BiomeRegistryEntryElementEffectMusic {
    boolean replacesCurrentMusic();
    String getSound();
    int getMaxDelay();
    int getMinDelay();
}

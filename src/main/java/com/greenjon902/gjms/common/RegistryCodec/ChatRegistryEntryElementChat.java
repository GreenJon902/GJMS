package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface ChatRegistryEntryElementChat {
    @Nullable ChatRegistryEntryElementChatDecorations getDecorations();

    default boolean hasDecorations() {
        return getDecorations() != null;
    }
}

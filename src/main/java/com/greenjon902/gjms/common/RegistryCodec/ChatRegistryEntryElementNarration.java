package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface ChatRegistryEntryElementNarration {
    @Nullable ChatRegistryEntryElementNarrationDecorations getDecorations();
    String getPriority();

    default boolean hasDecorations() {
        return getDecorations() != null;
    }
}

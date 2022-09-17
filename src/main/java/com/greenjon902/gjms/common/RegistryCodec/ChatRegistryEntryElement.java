package com.greenjon902.gjms.common.RegistryCodec;

import org.jetbrains.annotations.Nullable;

public interface ChatRegistryEntryElement {
    @Nullable ChatRegistryEntryElementChat getChat();
    @Nullable ChatRegistryEntryElementNarration getNarration();
    @Nullable ChatRegistryEntryElementOverlay getOverlay();

    default boolean hasChat() {
        return getChat() != null;
    }

    default boolean hasNarration() {
        return getNarration() != null;
    }

    default boolean hasOverlay() {
        return getOverlay() != null;
    }
}

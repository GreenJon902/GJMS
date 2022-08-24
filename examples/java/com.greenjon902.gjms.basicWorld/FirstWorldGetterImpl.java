package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.common.FirstWorldGetter;

import java.util.UUID;

public class FirstWorldGetterImpl implements FirstWorldGetter {
    @Override
    public String getWorld(UUID playerId) {
        return "BasicWorld";
    }
}

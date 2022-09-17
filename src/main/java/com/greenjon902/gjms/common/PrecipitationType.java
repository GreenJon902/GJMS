package com.greenjon902.gjms.common;

public enum PrecipitationType {
    RAIN("rain"), SNOW("snow"), NONE("none");

    private final String name;
    PrecipitationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package com.wisdom.farm.enums;

public enum ServiceType {
    WATER("浇水"),
    FERTILIZE("施肥");

    private final String label;

    ServiceType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static ServiceType parse(String value) {
        if (value == null || value.isBlank()) {
            return WATER;
        }
        return ServiceType.valueOf(value.trim().toUpperCase());
    }
}

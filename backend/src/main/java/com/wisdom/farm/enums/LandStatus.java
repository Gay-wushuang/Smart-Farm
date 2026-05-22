package com.wisdom.farm.enums;

public enum LandStatus {
    AVAILABLE(1, "可认领"),
    CLAIMED(2, "已认领");

    private final int dbValue;
    private final String label;

    LandStatus(int dbValue, String label) {
        this.dbValue = dbValue;
        this.label = label;
    }

    public int dbValue() {
        return dbValue;
    }

    public String label() {
        return label;
    }

    public static LandStatus fromDb(Integer status) {
        return status != null && status == CLAIMED.dbValue ? CLAIMED : AVAILABLE;
    }
}

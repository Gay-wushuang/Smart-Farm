package com.wisdom.farm.enums;

public enum MonitorRange {
    H24("24h"),
    D7("7d"),
    D30("30d");

    private final String apiValue;

    MonitorRange(String apiValue) {
        this.apiValue = apiValue;
    }

    public String apiValue() {
        return apiValue;
    }
}

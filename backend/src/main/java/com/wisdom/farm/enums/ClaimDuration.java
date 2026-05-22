package com.wisdom.farm.enums;

public enum ClaimDuration {
    ONE_MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    TWELVE_MONTH(12);

    private final int months;

    ClaimDuration(int months) {
        this.months = months;
    }

    public int months() {
        return months;
    }

    public static int normalize(Integer months) {
        if (months == null) {
            return TWELVE_MONTH.months;
        }
        for (ClaimDuration duration : values()) {
            if (duration.months == months) {
                return duration.months;
            }
        }
        throw new IllegalArgumentException("认领时长仅支持1、3、6、12个月");
    }
}

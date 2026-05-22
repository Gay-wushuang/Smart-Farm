package com.wisdom.farm.enums;

public enum OrderStatus {
    PENDING_PAY("待支付"),
    PAID("已支付"),
    PROCESSING("处理中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static OrderStatus fromDb(Integer status) {
        if (status != null && status == 2) {
            return CANCELLED;
        }
        if (status != null && status == 1) {
            return PAID;
        }
        return PENDING_PAY;
    }

    public static OrderStatus parse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return OrderStatus.valueOf(value.trim().toUpperCase());
    }
}

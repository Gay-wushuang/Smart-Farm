package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlertVO {
    private Long alertId;
    private String metric;
    private BigDecimal value;
    private String message;
}

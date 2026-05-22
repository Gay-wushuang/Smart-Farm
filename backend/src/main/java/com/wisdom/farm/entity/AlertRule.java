package com.wisdom.farm.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlertRule {
    private Long id;
    private Long landId;
    private String metric;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private Integer enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

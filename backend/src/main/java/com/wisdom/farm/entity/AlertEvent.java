package com.wisdom.farm.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlertEvent {
    private Long id;
    private Long landId;
    private String metric;
    private BigDecimal value;
    private String message;
    private Integer handled;
    private LocalDateTime createTime;
}

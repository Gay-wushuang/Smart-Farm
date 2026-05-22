package com.wisdom.farm.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlertDetailVO {
    private Long alertId;
    private Long landId;
    private String metric;
    private java.math.BigDecimal value;
    private String message;
    private Integer handled;
    private LocalDateTime createTime;
}

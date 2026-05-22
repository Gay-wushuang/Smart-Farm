package com.wisdom.farm.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MonitorData {
    private Long id;
    private Long landId;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal soilMoisture;
    private BigDecimal lightIntensity;
    private LocalDateTime createTime;
}

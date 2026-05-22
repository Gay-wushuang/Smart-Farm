package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MonitorDataVO {
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal soilMoisture;
    private BigDecimal lightIntensity;
    private LocalDateTime updateTime;
}

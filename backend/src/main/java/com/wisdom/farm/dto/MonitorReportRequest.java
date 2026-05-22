package com.wisdom.farm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonitorReportRequest {
    private Long landId;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal soilMoisture;
    private BigDecimal lightIntensity;
}

package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LandVO {
    private Long landId;
    private String landName;
    private String zone;
    private BigDecimal area;
    private String areaUnit;
    private String image;
    private List<String> images;
    private String status;
    private String statusName;
    private BigDecimal price;
    private String priceUnit;
    private String soilType;
    private String cropType;
    private String description;
    private Object claimUser;
    private LocalDateTime claimTime;
    private LocalDateTime claimExpireTime;
    private MonitorDataVO latestMonitor;
    private ServicePriceVO servicePrice;
}

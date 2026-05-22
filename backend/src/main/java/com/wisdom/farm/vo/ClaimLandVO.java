package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimLandVO {
    private Long claimId;
    private Long landId;
    private String landName;
    private String landImage;
    private BigDecimal area;
    private String areaUnit;
    private LocalDateTime claimTime;
    private LocalDateTime expireTime;
    private String status;
}

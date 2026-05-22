package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimResultVO {
    private Long claimId;
    private Long landId;
    private String landName;
    private Integer duration;
    private BigDecimal amount;
    private String paymentNo;
    private String status;
    private LocalDateTime expireTime;
}

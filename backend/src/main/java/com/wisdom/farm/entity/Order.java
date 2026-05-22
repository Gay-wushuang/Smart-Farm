package com.wisdom.farm.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long landId;
    private BigDecimal amount;
    private Integer status;
    private String statusText;
    private String serviceType;
    private Integer quantity;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime acceptTime;
    private LocalDateTime completeTime;
    private Long operatorId;
    private String operatorName;
    private String completeImages;
    private String completeRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

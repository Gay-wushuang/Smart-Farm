package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long orderId;
    private String orderNo;
    private Long userId;
    private Long landId;
    private String landName;
    private String serviceType;
    private String serviceTypeName;
    private Integer quantity;
    private BigDecimal amount;
    private String remark;
    private String status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime updateTime;
    private LocalDateTime acceptTime;
    private LocalDateTime completeTime;
    private OperatorVO operator;
    private List<String> completeImages;
    private String completeRemark;
}

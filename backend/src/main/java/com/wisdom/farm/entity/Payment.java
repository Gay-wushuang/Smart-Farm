package com.wisdom.farm.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Long id;
    private String paymentNo;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String payType;
    private String status;
    private String payMethod;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

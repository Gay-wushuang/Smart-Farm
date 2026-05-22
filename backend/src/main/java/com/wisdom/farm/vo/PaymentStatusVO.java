package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentStatusVO {
    private Long paymentId;
    private String paymentNo;
    private BigDecimal amount;
    private String status;
    private String statusName;
    private LocalDateTime payTime;
    private String payMethod;
}

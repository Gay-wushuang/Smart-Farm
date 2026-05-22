package com.wisdom.farm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateRequest {
    private Long orderId;
    private BigDecimal amount;
    private String payType;
}

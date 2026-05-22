package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateVO {
    private Long paymentId;
    private String paymentNo;
    private BigDecimal amount;
    private WechatPayParamsVO wxPayParams;
}

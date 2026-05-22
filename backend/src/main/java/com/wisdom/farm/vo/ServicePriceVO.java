package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicePriceVO {
    private BigDecimal waterPrice;
    private BigDecimal fertilizePrice;
    private String priceUnit;
}

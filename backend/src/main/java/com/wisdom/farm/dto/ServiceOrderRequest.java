package com.wisdom.farm.dto;

import lombok.Data;

@Data
public class ServiceOrderRequest {
    private Long landId;
    private String serviceType;
    private Integer quantity;
    private String remark;
}

package com.wisdom.farm.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompleteOrderRequest {
    private List<String> completeImages;
    private String completeRemark;
}

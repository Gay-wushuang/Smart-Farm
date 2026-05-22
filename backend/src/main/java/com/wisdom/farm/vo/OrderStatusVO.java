package com.wisdom.farm.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderStatusVO {
    private Long orderId;
    private String status;
    private String statusName;
    private LocalDateTime acceptTime;
}

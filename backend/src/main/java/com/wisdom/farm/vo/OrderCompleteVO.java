package com.wisdom.farm.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderCompleteVO {
    private Long orderId;
    private String status;
    private String statusName;
    private LocalDateTime completeTime;
    private List<String> completeImages;
    private String completeRemark;
}

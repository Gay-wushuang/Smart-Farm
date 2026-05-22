package com.wisdom.farm.vo;

import lombok.Data;

@Data
public class ReleaseLandVO {
    private Long landId;
    private String status;
    private String statusName;
    private String reason;
}

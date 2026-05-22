package com.wisdom.farm.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceVO {
    private Long deviceId;
    private Long landId;
    private String name;
    private String type;
    private String sn;
    private Integer status;
    private LocalDateTime createTime;
}

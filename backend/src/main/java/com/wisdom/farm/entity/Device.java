package com.wisdom.farm.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Device {
    private Long id;
    private Long landId;
    private String name;
    private String type;
    private String sn;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

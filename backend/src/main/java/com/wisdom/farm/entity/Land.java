package com.wisdom.farm.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Land {
    private Long id;
    private String name;
    private String location;
    private BigDecimal area;
    private BigDecimal price;
    private String description;
    private String images;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

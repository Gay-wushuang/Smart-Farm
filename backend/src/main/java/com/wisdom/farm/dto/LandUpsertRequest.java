package com.wisdom.farm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LandUpsertRequest {
    private String landName;
    private String zone;
    private BigDecimal area;
    private BigDecimal price;
    private String description;
    private List<String> images;
}

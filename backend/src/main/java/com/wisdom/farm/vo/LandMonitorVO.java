package com.wisdom.farm.vo;

import lombok.Data;

import java.util.List;

@Data
public class LandMonitorVO {
    private Long landId;
    private String landName;
    private MonitorDataVO latest;
    private List<MonitorDataVO> history;
}

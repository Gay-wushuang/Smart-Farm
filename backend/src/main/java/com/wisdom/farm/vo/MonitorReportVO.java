package com.wisdom.farm.vo;

import lombok.Data;

import java.util.List;

@Data
public class MonitorReportVO {
    private Long landId;
    private MonitorDataVO latest;
    private List<AlertVO> alerts;
}

package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.dto.MonitorReportRequest;
import com.wisdom.farm.entity.AlertEvent;
import com.wisdom.farm.entity.AlertRule;
import com.wisdom.farm.entity.Device;
import com.wisdom.farm.entity.MonitorData;
import com.wisdom.farm.mapper.AlertEventMapper;
import com.wisdom.farm.mapper.AlertRuleMapper;
import com.wisdom.farm.mapper.DeviceMapper;
import com.wisdom.farm.mapper.MonitorDataMapper;
import com.wisdom.farm.utils.ApiViewUtil;
import com.wisdom.farm.vo.AlertVO;
import com.wisdom.farm.vo.MonitorReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "监测数据", description = "监测数据相关接口")
@RestController
public class MonitorController {
    private final MonitorDataMapper monitorDataMapper;
    private final DeviceMapper deviceMapper;
    private final AlertRuleMapper alertRuleMapper;
    private final AlertEventMapper alertEventMapper;

    public MonitorController(MonitorDataMapper monitorDataMapper, DeviceMapper deviceMapper, AlertRuleMapper alertRuleMapper, AlertEventMapper alertEventMapper) {
        this.monitorDataMapper = monitorDataMapper;
        this.deviceMapper = deviceMapper;
        this.alertRuleMapper = alertRuleMapper;
        this.alertEventMapper = alertEventMapper;
    }

    @Operation(summary = "上报监测数据")
    @PostMapping("/monitor/report")
    public Result<MonitorReportVO> report(@RequestHeader("X-Device-SN") String deviceSn,
                                          @RequestHeader("X-Device-Token") String deviceToken,
                                          @RequestBody MonitorReportRequest request) {
        Device device = deviceMapper.selectBySn(deviceSn);
        if (device == null || device.getStatus() == null || device.getStatus() != 1 || device.getSecret() == null || !device.getSecret().equals(deviceToken)) {
            throw new SecurityException("设备鉴权失败");
        }
        if (!device.getLandId().equals(request.getLandId())) {
            throw new IllegalArgumentException("设备与土地不匹配");
        }
        MonitorData data = new MonitorData();
        data.setLandId(request.getLandId());
        data.setTemperature(request.getTemperature());
        data.setHumidity(request.getHumidity());
        data.setSoilMoisture(request.getSoilMoisture());
        data.setLightIntensity(request.getLightIntensity());
        monitorDataMapper.insert(data);

        MonitorReportVO result = new MonitorReportVO();
        result.setLandId(data.getLandId());
        result.setLatest(ApiViewUtil.monitorLatest(monitorDataMapper.latest(data.getLandId())));
        result.setAlerts(checkAlerts(data));
        return Result.success(result);
    }

    private List<AlertVO> checkAlerts(MonitorData data) {
        List<AlertVO> alerts = new ArrayList<>();
        for (AlertRule rule : alertRuleMapper.selectEnabled(data.getLandId())) {
            BigDecimal value = metricValue(data, rule.getMetric());
            if (value == null) {
                continue;
            }
            boolean lower = rule.getMinValue() != null && value.compareTo(rule.getMinValue()) < 0;
            boolean higher = rule.getMaxValue() != null && value.compareTo(rule.getMaxValue()) > 0;
            if (lower || higher) {
                String message = rule.getMetric() + "告警：" + value;
                AlertEvent event = new AlertEvent();
                event.setLandId(data.getLandId());
                event.setMetric(rule.getMetric());
                event.setValue(value);
                event.setMessage(message);
                event.setHandled(0);
                alertEventMapper.insert(event);

                AlertVO alert = new AlertVO();
                alert.setAlertId(event.getId());
                alert.setMetric(rule.getMetric());
                alert.setValue(value);
                alert.setMessage(message);
                alerts.add(alert);
            }
        }
        return alerts;
    }

    private BigDecimal metricValue(MonitorData data, String metric) {
        return switch (metric) {
            case "temperature" -> data.getTemperature();
            case "humidity" -> data.getHumidity();
            case "soilMoisture" -> data.getSoilMoisture();
            case "lightIntensity" -> data.getLightIntensity();
            default -> null;
        };
    }
}

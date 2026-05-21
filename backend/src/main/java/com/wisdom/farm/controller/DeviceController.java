package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "设备管理", description = "设备相关接口")
@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Operation(summary = "获取设备列表")
    @GetMapping("/list")
    public Result<Object> getDeviceList() {
        return Result.success();
    }

    @Operation(summary = "控制设备（浇水/施肥）")
    @PostMapping("/control")
    public Result<Object> controlDevice() {
        return Result.success();
    }

    @Operation(summary = "获取监控画面")
    @GetMapping("/monitor/{deviceId}")
    public Result<Object> getMonitorUrl(@PathVariable Long deviceId) {
        return Result.success();
    }
}

package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "土地管理", description = "土地相关接口")
@RestController
@RequestMapping("/api/land")
public class LandController {

    @Operation(summary = "获取土地列表")
    @GetMapping("/list")
    public Result<Object> getLandList() {
        return Result.success();
    }

    @Operation(summary = "获取土地详情")
    @GetMapping("/detail/{id}")
    public Result<Object> getLandDetail(@PathVariable Long id) {
        return Result.success();
    }
}

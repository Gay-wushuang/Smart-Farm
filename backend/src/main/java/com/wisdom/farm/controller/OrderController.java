package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Operation(summary = "创建认领订单")
    @PostMapping("/create")
    public Result<Object> createOrder() {
        return Result.success();
    }

    @Operation(summary = "获取订单列表")
    @GetMapping("/list")
    public Result<Object> getOrderList() {
        return Result.success();
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/detail/{id}")
    public Result<Object> getOrderDetail(@PathVariable Long id) {
        return Result.success();
    }
}

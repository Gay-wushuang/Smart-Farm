package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.dto.ServiceOrderRequest;
import com.wisdom.farm.service.OrderService;
import com.wisdom.farm.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "创建服务订单")
    @PostMapping
    public Result<OrderVO> createOrder(@RequestBody ServiceOrderRequest request) {
        try {
            return Result.success(orderService.createServiceOrder(UserContext.requireUserId(), request));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderId}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long orderId) {
        try {
            return Result.success(orderService.getDetail(UserContext.requireUserId(), orderId));
        } catch (IllegalArgumentException e) {
            return Result.error("订单不存在");
        }
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{orderId}/cancel")
    public Result<Object> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancel(UserContext.requireUserId(), orderId);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
}

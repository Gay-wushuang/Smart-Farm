package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.dto.CompleteOrderRequest;
import com.wisdom.farm.dto.LandUpsertRequest;
import com.wisdom.farm.dto.ReleaseLandRequest;
import com.wisdom.farm.service.LandService;
import com.wisdom.farm.service.OrderService;
import com.wisdom.farm.vo.AdminStatsVO;
import com.wisdom.farm.vo.LandVO;
import com.wisdom.farm.vo.OrderCompleteVO;
import com.wisdom.farm.vo.OrderStatusVO;
import com.wisdom.farm.vo.OrderVO;
import com.wisdom.farm.vo.PageResult;
import com.wisdom.farm.vo.ReleaseLandVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员", description = "管理员相关接口")
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final OrderService orderService;
    private final LandService landService;

    public AdminController(OrderService orderService, LandService landService) {
        this.orderService = orderService;
        this.landService = landService;
    }

    @ModelAttribute
    public void requireAdmin() {
        UserContext.requireAdmin();
    }

    @Operation(summary = "获取待处理订单列表")
    @GetMapping("/orders/pending")
    public Result<PageResult<OrderVO>> pendingOrders(@RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String serviceType) {
        return Result.success(orderService.adminList(page, size, "PENDING_PAY", serviceType, null, null));
    }

    @Operation(summary = "获取所有订单列表")
    @GetMapping("/orders")
    public Result<PageResult<OrderVO>> allOrders(@RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(required = false) String serviceType,
                                                 @RequestParam(required = false) String startDate,
                                                 @RequestParam(required = false) String endDate) {
        return Result.success(orderService.adminList(page, size, status, serviceType, startDate, endDate));
    }

    @Operation(summary = "管理员接单")
    @PutMapping("/orders/{orderId}/accept")
    public Result<OrderStatusVO> acceptOrder(@PathVariable Long orderId) {
        return Result.success(orderService.updateStatus(orderId, "PROCESSING"));
    }

    @Operation(summary = "完成服务")
    @PutMapping("/orders/{orderId}/complete")
    public Result<OrderCompleteVO> completeOrder(@PathVariable Long orderId, @RequestBody(required = false) CompleteOrderRequest request) {
        return Result.success(orderService.complete(orderId, request));
    }

    @Operation(summary = "新增土地")
    @PostMapping("/lands")
    public Result<LandVO> createLand(@RequestBody LandUpsertRequest request) {
        return Result.success(landService.create(request));
    }

    @Operation(summary = "修改土地信息")
    @PutMapping("/lands/{landId}")
    public Result<LandVO> updateLand(@PathVariable Long landId, @RequestBody LandUpsertRequest request) {
        return Result.success(landService.update(landId, request));
    }

    @Operation(summary = "释放土地")
    @PutMapping("/lands/{landId}/release")
    public Result<ReleaseLandVO> releaseLand(@PathVariable Long landId, @RequestBody(required = false) ReleaseLandRequest request) {
        landService.release(landId);
        ReleaseLandVO result = new ReleaseLandVO();
        result.setLandId(landId);
        result.setStatus("AVAILABLE");
        result.setStatusName("可认领");
        result.setReason(request == null ? "" : request.getReason());
        return Result.success(result);
    }

    @Operation(summary = "数据统计")
    @GetMapping("/stats")
    public Result<AdminStatsVO> stats() {
        return Result.success(orderService.adminStats());
    }
}

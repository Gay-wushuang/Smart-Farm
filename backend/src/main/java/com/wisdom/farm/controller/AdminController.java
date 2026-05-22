package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.dto.ChangePasswordRequest;
import com.wisdom.farm.dto.CompleteOrderRequest;
import com.wisdom.farm.dto.HandleAlertRequest;
import com.wisdom.farm.dto.LandUpsertRequest;
import com.wisdom.farm.dto.ReleaseLandRequest;
import com.wisdom.farm.dto.ResetPasswordRequest;
import com.wisdom.farm.dto.UpdateStatusRequest;
import com.wisdom.farm.entity.AdminUser;
import com.wisdom.farm.entity.AlertEvent;
import com.wisdom.farm.entity.Device;
import com.wisdom.farm.mapper.AdminUserMapper;
import com.wisdom.farm.mapper.AlertEventMapper;
import com.wisdom.farm.mapper.DeviceMapper;
import com.wisdom.farm.service.LandService;
import com.wisdom.farm.service.OrderService;
import com.wisdom.farm.vo.AdminInfoVO;
import com.wisdom.farm.vo.AdminStatsVO;
import com.wisdom.farm.vo.AlertDetailVO;
import com.wisdom.farm.vo.DeviceVO;
import com.wisdom.farm.vo.LandVO;
import com.wisdom.farm.vo.OrderCompleteVO;
import com.wisdom.farm.vo.OrderStatusVO;
import com.wisdom.farm.vo.OrderVO;
import com.wisdom.farm.vo.PageResult;
import com.wisdom.farm.vo.ReleaseLandVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "管理员", description = "管理员相关接口")
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final OrderService orderService;
    private final LandService landService;
    private final AdminUserMapper adminUserMapper;
    private final AlertEventMapper alertEventMapper;
    private final DeviceMapper deviceMapper;

    public AdminController(OrderService orderService, LandService landService,
                           AdminUserMapper adminUserMapper, AlertEventMapper alertEventMapper,
                           DeviceMapper deviceMapper) {
        this.orderService = orderService;
        this.landService = landService;
        this.adminUserMapper = adminUserMapper;
        this.alertEventMapper = alertEventMapper;
        this.deviceMapper = deviceMapper;
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

    @Operation(summary = "管理员修改自己的密码")
    @PutMapping("/password/change")
    public Result<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        Long adminId = UserContext.getUserId();
        AdminUser admin = adminUserMapper.selectById(adminId);
        if (admin == null) {
            return Result.error("管理员不存在");
        }
        String oldHash = DigestUtils.md5DigestAsHex(request.getOldPassword().getBytes());
        if (!oldHash.equals(admin.getPasswordHash())) {
            return Result.error("旧密码不正确");
        }
        String newHash = DigestUtils.md5DigestAsHex(request.getNewPassword().getBytes());
        adminUserMapper.updatePassword(adminId, newHash);
        return Result.success(null);
    }

    @Operation(summary = "超级管理员重置其他管理员密码")
    @PutMapping("/users/{adminId}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long adminId, @RequestBody ResetPasswordRequest request) {
        Long currentUserId = UserContext.getUserId();
        AdminUser currentAdmin = adminUserMapper.selectById(currentUserId);
        if (currentAdmin == null || !"SUPER_ADMIN".equals(currentAdmin.getRole())) {
            return Result.error("无权限操作");
        }
        AdminUser targetAdmin = adminUserMapper.selectById(adminId);
        if (targetAdmin == null) {
            return Result.error("目标管理员不存在");
        }
        String newHash = DigestUtils.md5DigestAsHex(request.getNewPassword().getBytes());
        adminUserMapper.updatePassword(adminId, newHash);
        return Result.success(null);
    }

    @Operation(summary = "管理员列表")
    @GetMapping("/users")
    public Result<List<AdminInfoVO>> userList() {
        List<AdminUser> list = adminUserMapper.selectAll();
        List<AdminInfoVO> voList = list.stream().map(admin -> {
            AdminInfoVO vo = new AdminInfoVO();
            vo.setAdminId(admin.getId());
            vo.setUsername(admin.getUsername());
            vo.setRole(admin.getRole());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Operation(summary = "启用/禁用管理员账号")
    @PutMapping("/users/{adminId}/status")
    public Result<Void> updateUserStatus(@PathVariable Long adminId, @RequestBody UpdateStatusRequest request) {
        Long currentUserId = UserContext.getUserId();
        AdminUser currentAdmin = adminUserMapper.selectById(currentUserId);
        if (currentAdmin == null || !"SUPER_ADMIN".equals(currentAdmin.getRole())) {
            return Result.error("无权限操作");
        }
        adminUserMapper.updateStatus(adminId, request.getStatus());
        return Result.success(null);
    }

    @Operation(summary = "告警事件列表")
    @GetMapping("/alerts")
    public Result<List<AlertDetailVO>> alertList(@RequestParam(required = false) Integer handled) {
        List<AlertEvent> list = alertEventMapper.selectList(handled);
        List<AlertDetailVO> voList = list.stream().map(event -> {
            AlertDetailVO vo = new AlertDetailVO();
            vo.setAlertId(event.getId());
            vo.setLandId(event.getLandId());
            vo.setMetric(event.getMetric());
            vo.setValue(event.getValue());
            vo.setMessage(event.getMessage());
            vo.setHandled(event.getHandled());
            vo.setCreateTime(event.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Operation(summary = "处理告警")
    @PutMapping("/alerts/{alertId}/handle")
    public Result<Void> handleAlert(@PathVariable Long alertId, @RequestBody HandleAlertRequest request) {
        AlertEvent event = alertEventMapper.selectById(alertId);
        if (event == null) {
            return Result.error("告警事件不存在");
        }
        alertEventMapper.updateHandled(alertId, 1);
        return Result.success(null);
    }

    @Operation(summary = "设备列表")
    @GetMapping("/devices")
    public Result<List<DeviceVO>> deviceList() {
        List<Device> list = deviceMapper.selectAll();
        List<DeviceVO> voList = list.stream().map(device -> {
            DeviceVO vo = new DeviceVO();
            vo.setDeviceId(device.getId());
            vo.setLandId(device.getLandId());
            vo.setName(device.getName());
            vo.setType(device.getType());
            vo.setSn(device.getSn());
            vo.setStatus(device.getStatus());
            vo.setCreateTime(device.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Operation(summary = "重置设备密钥")
    @PutMapping("/devices/{deviceId}/reset-secret")
    public Result<String> resetDeviceSecret(@PathVariable Long deviceId) {
        Device device = deviceMapper.selectById(deviceId);
        if (device == null) {
            return Result.error("设备不存在");
        }
        String newSecret = UUID.randomUUID().toString().replace("-", "");
        deviceMapper.resetSecret(deviceId, newSecret);
        return Result.success(newSecret);
    }
}

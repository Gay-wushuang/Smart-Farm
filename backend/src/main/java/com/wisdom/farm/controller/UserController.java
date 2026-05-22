package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.config.JwtProperties;
import com.wisdom.farm.dto.AdminLoginRequest;
import com.wisdom.farm.dto.BindPhoneRequest;
import com.wisdom.farm.dto.UserUpdateRequest;
import com.wisdom.farm.dto.WxLoginRequest;
import com.wisdom.farm.entity.AdminUser;
import com.wisdom.farm.mapper.AdminUserMapper;
import com.wisdom.farm.service.LandService;
import com.wisdom.farm.service.OrderService;
import com.wisdom.farm.service.UserService;
import com.wisdom.farm.utils.JwtUtil;
import com.wisdom.farm.utils.PasswordHasher;
import com.wisdom.farm.vo.AdminInfoVO;
import com.wisdom.farm.vo.AdminLoginResult;
import com.wisdom.farm.vo.ClaimLandVO;
import com.wisdom.farm.vo.LoginResult;
import com.wisdom.farm.vo.OrderVO;
import com.wisdom.farm.vo.PageResult;
import com.wisdom.farm.vo.PhoneBindResult;
import com.wisdom.farm.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证/用户", description = "认证与用户相关接口")
@RestController
public class UserController {
    private final UserService userService;
    private final LandService landService;
    private final OrderService orderService;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final AdminUserMapper adminUserMapper;
    private final PasswordHasher passwordHasher;

    public UserController(UserService userService, LandService landService, OrderService orderService, JwtUtil jwtUtil, JwtProperties jwtProperties, AdminUserMapper adminUserMapper, PasswordHasher passwordHasher) {
        this.userService = userService;
        this.landService = landService;
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
        this.adminUserMapper = adminUserMapper;
        this.passwordHasher = passwordHasher;
    }

    @Operation(summary = "微信登录")
    @PostMapping("/auth/wx-login")
    public Result<LoginResult> login(@RequestBody WxLoginRequest request) {
        try {
            return Result.success(userService.login(request.getCode()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "绑定手机号")
    @PostMapping("/auth/bind-phone")
    public Result<PhoneBindResult> bindPhone(@RequestBody BindPhoneRequest request) {
        return Result.success(userService.bindPhone(request.getCode()));
    }

    @Operation(summary = "管理员登录")
    @PostMapping("/auth/admin-login")
    public Result<AdminLoginResult> adminLogin(@RequestBody AdminLoginRequest request) {
        AdminUser admin = adminUserMapper.selectByUsername(request.getUsername());
        String storedPassword = admin == null || admin.getPasswordHash() == null || admin.getPasswordHash().isBlank()
                ? admin == null ? null : admin.getPassword()
                : admin.getPasswordHash();
        if (admin == null || admin.getStatus() == null || admin.getStatus() != 1 || !passwordHasher.matches(request.getPassword(), storedPassword)) {
            return Result.error("管理员账号或密码错误");
        }
        AdminInfoVO adminInfo = new AdminInfoVO();
        adminInfo.setAdminId(admin.getId());
        adminInfo.setUsername(admin.getUsername());
        adminInfo.setRole(admin.getRole());

        AdminLoginResult result = new AdminLoginResult();
        result.setToken(jwtUtil.createToken(admin.getId(), admin.getRole()));
        result.setTokenExpire(jwtProperties.expireTimeFromNow());
        result.setAdminInfo(adminInfo);
        return Result.success(result);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/user/profile")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(userService.getCurrentUser());
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/user/profile")
    public Result<UserInfoVO> updateUserInfo(@RequestBody UserUpdateRequest request) {
        return Result.success(userService.updateCurrentUser(request.getNickname(), request.getAvatar()));
    }

    @Operation(summary = "获取我的认领土地列表")
    @GetMapping("/user/lands")
    public Result<PageResult<ClaimLandVO>> getMyLands(@RequestParam(required = false) Integer page,
                                                      @RequestParam(required = false) Integer size) {
        return Result.success(orderService.claimLandPage(UserContext.requireUserId(), page, size));
    }

    @Operation(summary = "获取我的订单列表")
    @GetMapping("/user/orders")
    public Result<PageResult<OrderVO>> getMyOrders(@RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer size,
                                                   @RequestParam(required = false) String status) {
        return Result.success(orderService.listPage(UserContext.requireUserId(), page, size, status));
    }
}

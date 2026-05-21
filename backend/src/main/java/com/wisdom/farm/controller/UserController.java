package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Operation(summary = "微信登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody String code) {
        return Result.success("token");
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public Result<Object> getUserInfo() {
        return Result.success();
    }
}

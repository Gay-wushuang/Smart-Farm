package com.wisdom.farm.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminLoginResult {
    private String token;
    private LocalDateTime tokenExpire;
    private AdminInfoVO adminInfo;
}

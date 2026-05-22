package com.wisdom.farm.vo;

import com.wisdom.farm.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResult {
    private String token;
    private LocalDateTime tokenExpire;
    private Boolean isNewUser;
    private UserInfoVO userInfo;
}

package com.wisdom.farm.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer claimCount;
    private Integer orderCount;
    private LocalDateTime createTime;
}

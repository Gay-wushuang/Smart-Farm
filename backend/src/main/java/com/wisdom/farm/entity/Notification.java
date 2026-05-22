package com.wisdom.farm.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String type;
    private Boolean read;
    private String extra;
    private LocalDateTime createTime;
}

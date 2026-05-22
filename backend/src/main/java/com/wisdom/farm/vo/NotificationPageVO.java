package com.wisdom.farm.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationPageVO extends PageResult<NotificationVO> {
    private Long unreadCount;
}

package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.mapper.NotificationMapper;
import com.wisdom.farm.utils.ApiViewUtil;
import com.wisdom.farm.vo.NotificationPageVO;
import com.wisdom.farm.vo.NotificationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "通知", description = "通知相关接口")
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Operation(summary = "获取通知列表")
    @GetMapping
    public Result<NotificationPageVO> list(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) String type) {
        int currentPage = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        Long userId = UserContext.requireUserId();
        List<NotificationVO> list = notificationMapper.selectList(userId, type, (currentPage - 1) * pageSize, pageSize)
                .stream()
                .map(ApiViewUtil::notificationItem)
                .toList();
        NotificationPageVO data = new NotificationPageVO();
        data.setList(list);
        data.setTotal(notificationMapper.count(userId, type));
        data.setPage(currentPage);
        data.setSize(pageSize);
        data.setUnreadCount(notificationMapper.unreadCount(userId));
        return Result.success(data);
    }

    @Operation(summary = "标记通知已读")
    @PutMapping("/{notificationId}/read")
    public Result<Object> read(@PathVariable Long notificationId) {
        notificationMapper.markRead(UserContext.requireUserId(), notificationId);
        return Result.success();
    }

    @Operation(summary = "全部标记已读")
    @PutMapping("/read-all")
    public Result<Object> readAll() {
        notificationMapper.markAllRead(UserContext.requireUserId());
        return Result.success();
    }
}

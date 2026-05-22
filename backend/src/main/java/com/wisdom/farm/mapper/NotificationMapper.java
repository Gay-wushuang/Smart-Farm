package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    int insert(Notification notification);

    List<Notification> selectList(@Param("userId") Long userId,
                                  @Param("type") String type,
                                  @Param("offset") Integer offset,
                                  @Param("pageSize") Integer pageSize);

    long count(@Param("userId") Long userId, @Param("type") String type);

    long unreadCount(@Param("userId") Long userId);

    int markRead(@Param("userId") Long userId, @Param("id") Long id);

    int markAllRead(@Param("userId") Long userId);
}

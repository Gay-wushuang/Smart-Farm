package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.AlertEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertEventMapper {
    int insert(AlertEvent event);

    AlertEvent selectById(@Param("id") Long id);

    List<AlertEvent> selectList(@Param("handled") Integer handled);

    int updateHandled(@Param("id") Long id, @Param("handled") Integer handled);
}

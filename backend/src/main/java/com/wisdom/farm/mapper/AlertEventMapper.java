package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.AlertEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertEventMapper {
    int insert(AlertEvent event);
}

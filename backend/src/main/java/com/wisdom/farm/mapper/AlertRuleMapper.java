package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.AlertRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertRuleMapper {
    List<AlertRule> selectEnabled(@Param("landId") Long landId);
}

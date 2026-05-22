package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.MonitorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MonitorDataMapper {
    int insert(MonitorData data);

    MonitorData latest(@Param("landId") Long landId);

    List<MonitorData> history(@Param("landId") Long landId, @Param("since") LocalDateTime since);
}

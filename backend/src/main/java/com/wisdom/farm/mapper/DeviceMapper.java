package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceMapper {
    Device selectBySn(@Param("sn") String sn);
}

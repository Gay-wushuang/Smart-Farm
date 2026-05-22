package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    Device selectBySn(@Param("sn") String sn);

    Device selectById(@Param("id") Long id);

    List<Device> selectAll();

    int resetSecret(@Param("id") Long id, @Param("secret") String secret);
}

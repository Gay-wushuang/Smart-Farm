package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.Land;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LandMapper {
    List<Land> selectList(@Param("offset") Integer offset,
                          @Param("pageSize") Integer pageSize,
                          @Param("zone") String zone,
                          @Param("status") Integer status);

    Land selectById(@Param("id") Long id);

    long count(@Param("zone") String zone, @Param("status") Integer status);

    int insert(Land land);

    int update(Land land);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}

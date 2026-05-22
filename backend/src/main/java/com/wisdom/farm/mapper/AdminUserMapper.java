package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminUserMapper {
    AdminUser selectByUsername(@Param("username") String username);

    AdminUser selectById(@Param("id") Long id);
}

package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    AdminUser selectByUsername(@Param("username") String username);

    AdminUser selectById(@Param("id") Long id);

    List<AdminUser> selectAll();

    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}

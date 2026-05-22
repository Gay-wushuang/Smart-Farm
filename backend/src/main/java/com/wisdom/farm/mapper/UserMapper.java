package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByOpenid(@Param("openid") String openid);

    User selectById(@Param("id") Long id);

    int insert(User user);

    int updateProfile(@Param("id") Long id, @Param("nickname") String nickname, @Param("avatar") String avatar);

    int updatePhone(@Param("id") Long id, @Param("phone") String phone);

    long count();
}

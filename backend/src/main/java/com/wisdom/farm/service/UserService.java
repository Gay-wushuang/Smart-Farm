package com.wisdom.farm.service;

import com.wisdom.farm.entity.User;
import com.wisdom.farm.vo.LoginResult;
import com.wisdom.farm.vo.PhoneBindResult;
import com.wisdom.farm.vo.UserInfoVO;

public interface UserService {
    LoginResult login(String code);

    User getCurrentUserEntity();

    UserInfoVO getCurrentUser();

    UserInfoVO updateCurrentUser(String nickname, String avatar);

    PhoneBindResult bindPhone(String code);
}

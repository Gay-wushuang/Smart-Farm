package com.wisdom.farm.service.impl;

import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.config.JwtProperties;
import com.wisdom.farm.entity.Notification;
import com.wisdom.farm.entity.User;
import com.wisdom.farm.enums.NotificationType;
import com.wisdom.farm.mapper.NotificationMapper;
import com.wisdom.farm.mapper.UserMapper;
import com.wisdom.farm.service.UserService;
import com.wisdom.farm.service.WechatAuthService;
import com.wisdom.farm.utils.ApiViewUtil;
import com.wisdom.farm.utils.JwtUtil;
import com.wisdom.farm.vo.LoginResult;
import com.wisdom.farm.vo.PhoneBindResult;
import com.wisdom.farm.vo.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final WechatAuthService wechatAuthService;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final NotificationMapper notificationMapper;

    public UserServiceImpl(UserMapper userMapper, WechatAuthService wechatAuthService, JwtUtil jwtUtil, JwtProperties jwtProperties, NotificationMapper notificationMapper) {
        this.userMapper = userMapper;
        this.wechatAuthService = wechatAuthService;
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public LoginResult login(String code) {
        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("微信 code 不能为空");
        }

        String openid = wechatAuthService.getOpenid(code);
        User user = userMapper.selectByOpenid(openid);
        boolean isNewUser = user == null;
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname("农场用户");
            user.setStatus(1);
            userMapper.insert(user);
            createWelcomeNotification(user.getId());
        }

        LoginResult result = new LoginResult();
        result.setToken(jwtUtil.createToken(user.getId()));
        result.setTokenExpire(jwtProperties.expireTimeFromNow());
        result.setIsNewUser(isNewUser);
        result.setUserInfo(ApiViewUtil.userInfo(user));
        return result;
    }

    @Override
    public User getCurrentUserEntity() {
        return userMapper.selectById(UserContext.requireUserId());
    }

    @Override
    public UserInfoVO getCurrentUser() {
        User user = getCurrentUserEntity();
        UserInfoVO data = ApiViewUtil.userInfo(user);
        Long userId = UserContext.requireUserId();
        data.setClaimCount(userMapper.countClaimsByUserId(userId));
        data.setOrderCount(userMapper.countOrdersByUserId(userId));
        data.setCreateTime(user == null ? null : user.getCreateTime());
        return data;
    }

    @Override
    public UserInfoVO updateCurrentUser(String nickname, String avatar) {
        Long userId = UserContext.requireUserId();
        userMapper.updateProfile(userId, nickname, avatar);
        return getCurrentUser();
    }

    @Override
    public PhoneBindResult bindPhone(String code) {
        String phone = StringUtils.hasText(code) ? "wx_phone_" + code : "";
        userMapper.updatePhone(UserContext.requireUserId(), phone);
        PhoneBindResult result = new PhoneBindResult();
        result.setPhone(phone);
        return result;
    }

    private void createWelcomeNotification(Long userId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle("欢迎使用智慧农场");
        notification.setContent("系统已为你开启土地认领服务");
        notification.setType(NotificationType.SYSTEM.name());
        notification.setRead(false);
        notification.setExtra("{}");
        notificationMapper.insert(notification);
    }
}

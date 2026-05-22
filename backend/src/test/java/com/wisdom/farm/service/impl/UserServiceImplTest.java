package com.wisdom.farm.service.impl;

import com.wisdom.farm.config.JwtProperties;
import com.wisdom.farm.entity.User;
import com.wisdom.farm.mapper.NotificationMapper;
import com.wisdom.farm.mapper.UserMapper;
import com.wisdom.farm.service.WechatAuthService;
import com.wisdom.farm.utils.JwtUtil;
import com.wisdom.farm.vo.LoginResult;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Test
    void loginShouldCreateUserAndReturnJwt() {
        UserMapper userMapper = mock(UserMapper.class);
        WechatAuthService wechatAuthService = mock(WechatAuthService.class);
        JwtUtil jwtUtil = new JwtUtil(jwtProperties());
        when(wechatAuthService.getOpenid("wx-code")).thenReturn("openid-1");
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(7L);
            return 1;
        });

        JwtProperties properties = jwtProperties();
        jwtUtil = new JwtUtil(properties);
        UserServiceImpl service = new UserServiceImpl(userMapper, wechatAuthService, jwtUtil, properties, mock(NotificationMapper.class));
        LoginResult result = service.login("wx-code");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).insert(captor.capture());
        assertEquals("openid-1", captor.getValue().getOpenid());
        assertEquals(captor.getValue().getId(), result.getUserInfo().getUserId());
        assertEquals(captor.getValue().getId(), jwtUtil.parseUserId(result.getToken()));
    }

    @Test
    void loginShouldReuseExistingUser() {
        UserMapper userMapper = mock(UserMapper.class);
        WechatAuthService wechatAuthService = mock(WechatAuthService.class);
        JwtUtil jwtUtil = new JwtUtil(jwtProperties());
        User user = new User();
        user.setId(8L);
        user.setOpenid("openid-1");
        when(wechatAuthService.getOpenid("wx-code")).thenReturn("openid-1");
        when(userMapper.selectByOpenid("openid-1")).thenReturn(user);

        JwtProperties properties = jwtProperties();
        jwtUtil = new JwtUtil(properties);
        UserServiceImpl service = new UserServiceImpl(userMapper, wechatAuthService, jwtUtil, properties, mock(NotificationMapper.class));
        LoginResult result = service.login("wx-code");

        assertEquals(8L, jwtUtil.parseUserId(result.getToken()));
        assertEquals(8L, result.getUserInfo().getUserId());
    }

    private JwtProperties jwtProperties() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("unit-test-secret-at-least-32-bytes");
        properties.setExpireMinutes(10L);
        return properties;
    }
}

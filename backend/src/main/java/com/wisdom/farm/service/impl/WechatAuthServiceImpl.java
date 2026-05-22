package com.wisdom.farm.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisdom.farm.config.WechatProperties;
import com.wisdom.farm.service.WechatAuthService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    private final WechatProperties wechatProperties;
    private final RestClient restClient;

    public WechatAuthServiceImpl(WechatProperties wechatProperties, RestClient.Builder restClientBuilder) {
        this.wechatProperties = wechatProperties;
        this.restClient = restClientBuilder.build();
    }

    @Override
    public String getOpenid(String code) {
        if (useDevLoginFallback()) {
            if (!StringUtils.hasText(code)) {
                throw new IllegalArgumentException("微信code不能为空");
            }
            return "dev_openid_" + code;
        }

        WechatSessionResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.weixin.qq.com")
                        .path("/sns/jscode2session")
                        .queryParam("appid", wechatProperties.getAppid())
                        .queryParam("secret", wechatProperties.getSecret())
                        .queryParam("js_code", code)
                        .queryParam("grant_type", "authorization_code")
                        .build())
                .retrieve()
                .body(WechatSessionResponse.class);

        if (response == null) {
            throw new IllegalStateException("微信登录响应为空");
        }
        if (response.errcode != null && response.errcode != 0) {
            throw new IllegalArgumentException("微信登录失败：" + response.errmsg);
        }
        if (!StringUtils.hasText(response.openid)) {
            throw new IllegalArgumentException("微信登录未返回openid");
        }
        return response.openid;
    }

    private boolean useDevLoginFallback() {
        return !StringUtils.hasText(wechatProperties.getAppid())
                || !StringUtils.hasText(wechatProperties.getSecret())
                || "touristappid".equalsIgnoreCase(wechatProperties.getAppid())
                || wechatProperties.getAppid().startsWith("your_")
                || wechatProperties.getSecret().startsWith("your_");
    }

    private static final class WechatSessionResponse {
        public String openid;
        @JsonProperty("session_key")
        public String sessionKey;
        public String unionid;
        public Integer errcode;
        public String errmsg;
    }
}

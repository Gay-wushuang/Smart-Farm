package com.wisdom.farm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wisdom.jwt")
public class JwtProperties {
    private String secret = "change-this-secret-at-least-32-bytes";
    private Long expireMinutes = 10080L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpireMinutes() {
        return expireMinutes;
    }

    public void setExpireMinutes(Long expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    public java.time.LocalDateTime expireTimeFromNow() {
        return java.time.LocalDateTime.now().plusMinutes(expireMinutes);
    }
}

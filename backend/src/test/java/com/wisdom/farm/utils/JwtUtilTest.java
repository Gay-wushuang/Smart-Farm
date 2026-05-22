package com.wisdom.farm.utils;

import com.wisdom.farm.config.JwtProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {
    @Test
    void createTokenShouldBeParsedBackToUserId() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("unit-test-secret-at-least-32-bytes");
        properties.setExpireMinutes(10L);
        JwtUtil jwtUtil = new JwtUtil(properties);

        String token = jwtUtil.createToken(12L);

        assertEquals(12L, jwtUtil.parseUserId(token));
    }

    @Test
    void parseUserIdShouldRejectExpiredToken() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("unit-test-secret-at-least-32-bytes");
        properties.setExpireMinutes(-1L);
        JwtUtil jwtUtil = new JwtUtil(properties);

        String token = jwtUtil.createToken(12L);

        assertThrows(IllegalArgumentException.class, () -> jwtUtil.parseUserId(token));
    }
}

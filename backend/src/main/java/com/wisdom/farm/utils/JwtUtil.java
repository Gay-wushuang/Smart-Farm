package com.wisdom.farm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.farm.config.JwtProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(Long userId) {
        return createToken(userId, "USER");
    }

    public String createToken(Long userId, String role) {
        long now = Instant.now().getEpochSecond();
        long exp = now + jwtProperties.getExpireMinutes() * 60;
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", String.valueOf(userId));
        payload.put("role", role);
        payload.put("iat", now);
        payload.put("exp", exp);

        String unsignedToken = encodeJson(header) + "." + encodeJson(payload);
        return unsignedToken + "." + sign(unsignedToken);
    }

    public Long parseUserId(String token) {
        return parseClaims(token).userId();
    }

    public Claims parseClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("token不能为空");
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("token格式错误");
        }
        String unsignedToken = parts[0] + "." + parts[1];
        if (!constantTimeEquals(sign(unsignedToken), parts[2])) {
            throw new IllegalArgumentException("token签名无效");
        }

        Map<String, Object> payload = decodeJson(parts[1]);
        Number exp = (Number) payload.get("exp");
        if (exp == null || exp.longValue() < Instant.now().getEpochSecond()) {
            throw new IllegalArgumentException("token已过期");
        }
        Long userId = Long.valueOf(String.valueOf(payload.get("sub")));
        String role = String.valueOf(payload.getOrDefault("role", "USER"));
        return new Claims(userId, role);
    }

    public record Claims(Long userId, String role) {
        public boolean admin() {
            return "ADMIN".equalsIgnoreCase(role) || "SUPER_ADMIN".equalsIgnoreCase(role);
        }
    }

    private String encodeJson(Map<String, Object> data) {
        try {
            return URL_ENCODER.encodeToString(OBJECT_MAPPER.writeValueAsBytes(data));
        } catch (Exception e) {
            throw new IllegalStateException("JWT序列化失败", e);
        }
    }

    private Map<String, Object> decodeJson(String data) {
        try {
            byte[] bytes = URL_DECODER.decode(data);
            return OBJECT_MAPPER.readValue(bytes, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("token内容错误", e);
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            return URL_ENCODER.encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("JWT签名失败", e);
        }
    }

    private boolean constantTimeEquals(String expected, String actual) {
        byte[] left = expected.getBytes(StandardCharsets.UTF_8);
        byte[] right = actual.getBytes(StandardCharsets.UTF_8);
        if (left.length != right.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < left.length; i++) {
            result |= left[i] ^ right[i];
        }
        return result == 0;
    }
}

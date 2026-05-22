package com.wisdom.farm.utils;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordHasher {
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String hash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return "pbkdf2$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt) + "$" + derive(password, salt, ITERATIONS);
    }

    public boolean matches(String password, String stored) {
        if (stored == null || stored.isBlank()) {
            return false;
        }
        if (!stored.startsWith("pbkdf2$")) {
            return stored.equals(password);
        }
        String[] parts = stored.split("\\$");
        if (parts.length != 4) {
            return false;
        }
        int iterations = Integer.parseInt(parts[1]);
        byte[] salt = Base64.getDecoder().decode(parts[2]);
        return constantTimeEquals(parts[3], derive(password, salt, iterations));
    }

    private String derive(String password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("密码哈希失败", e);
        }
    }

    private boolean constantTimeEquals(String expected, String actual) {
        byte[] left = expected.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] right = actual.getBytes(java.nio.charset.StandardCharsets.UTF_8);
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

package com.wisdom.farm.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaSigner {
    public String signSha256WithRsa(String message, String privateKeyPemOrPath) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(loadPrivateKey(privateKeyPemOrPath));
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new IllegalStateException("RSA签名失败", e);
        }
    }

    public boolean verifySha256WithRsa(String message, String signatureValue, String publicKeyPemOrPath) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(loadPublicKey(publicKeyPemOrPath));
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signatureValue));
        } catch (Exception e) {
            return false;
        }
    }

    private PrivateKey loadPrivateKey(String pemOrPath) throws Exception {
        String pem = readPem(pemOrPath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    private PublicKey loadPublicKey(String pemOrPath) throws Exception {
        String pem = readPem(pemOrPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    private String readPem(String pemOrPath) throws Exception {
        Path path = Path.of(pemOrPath);
        if (Files.exists(path)) {
            return Files.readString(path, StandardCharsets.UTF_8);
        }
        return pemOrPath.replace("\\n", "\n");
    }
}

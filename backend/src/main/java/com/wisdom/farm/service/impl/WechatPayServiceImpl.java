package com.wisdom.farm.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.farm.config.WechatPayProperties;
import com.wisdom.farm.entity.Payment;
import com.wisdom.farm.service.WechatPayService;
import com.wisdom.farm.utils.RsaSigner;
import com.wisdom.farm.vo.WechatPayParamsVO;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WechatPayServiceImpl implements WechatPayService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final WechatPayProperties properties;
    private final RsaSigner rsaSigner;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public WechatPayServiceImpl(WechatPayProperties properties, RsaSigner rsaSigner) {
        this.properties = properties;
        this.rsaSigner = rsaSigner;
    }

    @Override
    public WechatPayParamsVO createJsapiPayParams(Payment payment, String openid) {
        String prepayId = properties.configured() ? requestPrepayId(payment, openid) : "dev_prepay_" + payment.getId();
        String timeStamp = String.valueOf(Instant.now().getEpochSecond());
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        String packageValue = "prepay_id=" + prepayId;
        String paySign = signPayParams(timeStamp, nonceStr, packageValue);
        WechatPayParamsVO data = new WechatPayParamsVO();
        data.setTimeStamp(timeStamp);
        data.setNonceStr(nonceStr);
        data.setPackageValue(packageValue);
        data.setSignType("RSA");
        data.setPaySign(paySign);
        return data;
    }

    @Override
    public boolean verifyNotify(String timestamp, String nonce, String body, String signature) {
        if (properties.getPlatformPublicKeyPath() == null || properties.getPlatformPublicKeyPath().isBlank()) {
            return !properties.configured();
        }
        String message = timestamp + "\n" + nonce + "\n" + body + "\n";
        return rsaSigner.verifySha256WithRsa(message, signature, properties.getPlatformPublicKeyPath());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> decryptNotifyResource(Map<?, ?> resource) {
        if (resource == null || resource.isEmpty()) {
            return Map.of();
        }
        if (properties.getApiV3Key() == null || properties.getApiV3Key().isBlank()) {
            throw new IllegalStateException("WECHAT_PAY_API_V3_KEY is required to decrypt notify resource");
        }
        try {
            String algorithm = String.valueOf(resource.get("algorithm"));
            if (!"AEAD_AES_256_GCM".equals(algorithm)) {
                throw new IllegalArgumentException("Unsupported WeChat Pay resource algorithm: " + algorithm);
            }
            byte[] cipherText = Base64.getDecoder().decode(String.valueOf(resource.get("ciphertext")));
            byte[] nonce = String.valueOf(resource.get("nonce")).getBytes(StandardCharsets.UTF_8);
            Object associatedDataValue = resource.get("associated_data");
            byte[] associatedData = associatedDataValue == null
                    ? new byte[0]
                    : String.valueOf(associatedDataValue).getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(properties.getApiV3Key().getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, nonce));
            cipher.updateAAD(associatedData);
            String json = new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid WeChat Pay notify resource", e);
        }
    }

    private String requestPrepayId(Payment payment, String openid) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("appid", properties.getAppid());
            body.put("mchid", properties.getMchId());
            body.put("description", "Wisdom farm order payment");
            body.put("out_trade_no", payment.getPaymentNo());
            body.put("notify_url", properties.getNotifyUrl());
            body.put("amount", Map.of("total", payment.getAmount().movePointRight(2).intValue(), "currency", "CNY"));
            body.put("payer", Map.of("openid", openid));
            String json = OBJECT_MAPPER.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", authorization("POST", "/v3/pay/transactions/jsapi", json))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("WeChat Pay create transaction failed: " + response.body());
            }
            JsonNode node = OBJECT_MAPPER.readTree(response.body());
            return node.path("prepay_id").asText();
        } catch (Exception e) {
            throw new IllegalStateException("WeChat Pay create transaction failed", e);
        }
    }

    private String authorization(String method, String canonicalUrl, String body) {
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String message = method + "\n" + canonicalUrl + "\n" + timestamp + "\n" + nonce + "\n" + body + "\n";
        String privateKey = properties.getPrivateKeyPath() == null || properties.getPrivateKeyPath().isBlank()
                ? properties.getPrivateKey()
                : properties.getPrivateKeyPath();
        String signature = rsaSigner.signSha256WithRsa(message, privateKey);
        return "WECHATPAY2-SHA256-RSA2048 mchid=\"" + properties.getMchId()
                + "\",nonce_str=\"" + nonce
                + "\",timestamp=\"" + timestamp
                + "\",serial_no=\"" + properties.getMerchantSerialNo()
                + "\",signature=\"" + signature + "\"";
    }

    private String signPayParams(String timeStamp, String nonceStr, String packageValue) {
        if (!properties.configured()) {
            return "dev-pay-sign";
        }
        String privateKey = properties.getPrivateKeyPath() == null || properties.getPrivateKeyPath().isBlank()
                ? properties.getPrivateKey()
                : properties.getPrivateKeyPath();
        return rsaSigner.signSha256WithRsa(properties.getAppid() + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageValue + "\n", privateKey);
    }
}

package com.wisdom.farm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wisdom.wechat-pay")
public class WechatPayProperties {
    private String mchId;
    private String appid;
    private String merchantSerialNo;
    private String privateKeyPath;
    private String privateKey;
    private String apiV3Key;
    private String platformPublicKeyPath;
    private String notifyUrl;

    public boolean configured() {
        return notBlank(mchId) && notBlank(appid) && notBlank(merchantSerialNo)
                && (notBlank(privateKeyPath) || notBlank(privateKey)) && notBlank(notifyUrl);
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMerchantSerialNo() {
        return merchantSerialNo;
    }

    public void setMerchantSerialNo(String merchantSerialNo) {
        this.merchantSerialNo = merchantSerialNo;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getApiV3Key() {
        return apiV3Key;
    }

    public void setApiV3Key(String apiV3Key) {
        this.apiV3Key = apiV3Key;
    }

    public String getPlatformPublicKeyPath() {
        return platformPublicKeyPath;
    }

    public void setPlatformPublicKeyPath(String platformPublicKeyPath) {
        this.platformPublicKeyPath = platformPublicKeyPath;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}

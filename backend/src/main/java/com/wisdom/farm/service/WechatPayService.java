package com.wisdom.farm.service;

import com.wisdom.farm.entity.Payment;
import com.wisdom.farm.vo.WechatPayParamsVO;

import java.util.Map;

public interface WechatPayService {
    WechatPayParamsVO createJsapiPayParams(Payment payment, String openid);

    boolean verifyNotify(String timestamp, String nonce, String body, String signature);

    Map<String, Object> decryptNotifyResource(Map<?, ?> resource);
}

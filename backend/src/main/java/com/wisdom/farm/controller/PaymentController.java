package com.wisdom.farm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.dto.PaymentCreateRequest;
import com.wisdom.farm.entity.Payment;
import com.wisdom.farm.mapper.PaymentMapper;
import com.wisdom.farm.mapper.UserMapper;
import com.wisdom.farm.service.WechatPayService;
import com.wisdom.farm.utils.ApiViewUtil;
import com.wisdom.farm.vo.PaymentCreateVO;
import com.wisdom.farm.vo.PaymentStatusVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Tag(name = "Payment", description = "Payment APIs")
@RestController
@RequestMapping("/pay")
public class PaymentController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final PaymentMapper paymentMapper;
    private final UserMapper userMapper;
    private final WechatPayService wechatPayService;

    public PaymentController(PaymentMapper paymentMapper, UserMapper userMapper, WechatPayService wechatPayService) {
        this.paymentMapper = paymentMapper;
        this.userMapper = userMapper;
        this.wechatPayService = wechatPayService;
    }

    @Operation(summary = "Create WeChat payment")
    @PostMapping("/create")
    public Result<PaymentCreateVO> create(@RequestBody PaymentCreateRequest request) {
        long paymentId = System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setPaymentNo("PAY" + paymentId);
        payment.setOrderId(request.getOrderId());
        payment.setUserId(UserContext.requireUserId());
        payment.setAmount(request.getAmount() == null ? java.math.BigDecimal.ZERO : request.getAmount());
        payment.setPayType(request.getPayType() == null ? "SERVICE" : request.getPayType());
        payment.setStatus("PENDING");
        payment.setPayMethod("WECHAT");
        paymentMapper.insert(payment);
        String openid = userMapper.selectById(payment.getUserId()).getOpenid();
        return Result.success(ApiViewUtil.paymentResult(payment, wechatPayService.createJsapiPayParams(payment, openid)));
    }

    @Operation(summary = "Get payment status")
    @GetMapping("/status/{paymentId}")
    public Result<PaymentStatusVO> status(@PathVariable Long paymentId) {
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null) {
            return Result.error("Payment record not found");
        }
        return Result.success(ApiViewUtil.paymentStatus(payment));
    }

    @Operation(summary = "WeChat payment notify")
    @PostMapping("/notify/wechat")
    public void notifyWechat(@RequestBody(required = false) String body, HttpServletRequest request) {
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String signature = request.getHeader("Wechatpay-Signature");
        if (!wechatPayService.verifyNotify(timestamp, nonce, body == null ? "" : body, signature)) {
            throw new SecurityException("WeChat Pay notify signature verification failed");
        }
        if (body == null || body.isBlank()) {
            return;
        }
        try {
            Map<?, ?> data = OBJECT_MAPPER.readValue(body.getBytes(StandardCharsets.UTF_8), Map.class);
            if (data.get("resource") instanceof Map<?, ?> resource) {
                data = wechatPayService.decryptNotifyResource(resource);
            }
            Object outTradeNo = data.get("out_trade_no");
            if (outTradeNo != null) {
                String tradeState = String.valueOf(data.get("trade_state"));
                paymentMapper.updateStatusByPaymentNo(String.valueOf(outTradeNo), toPaymentStatus(tradeState));
                return;
            }
            Object paymentId = data.get("paymentId");
            Object status = data.containsKey("status") ? data.get("status") : "SUCCESS";
            if (paymentId != null) {
                paymentMapper.updateStatus(Long.valueOf(String.valueOf(paymentId)), String.valueOf(status));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid WeChat Pay notify body", e);
        }
    }

    private String toPaymentStatus(String tradeState) {
        return "SUCCESS".equals(tradeState) ? "SUCCESS" : "FAILED";
    }
}

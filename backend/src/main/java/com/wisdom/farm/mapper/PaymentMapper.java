package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PaymentMapper {
    int insert(Payment payment);

    Payment selectById(@Param("id") Long id);

    Payment selectByPaymentNo(@Param("paymentNo") String paymentNo);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int updateStatusByPaymentNo(@Param("paymentNo") String paymentNo, @Param("status") String status);

    BigDecimal todayRevenue();

    BigDecimal monthRevenue();

    List<Payment> selectRecent(@Param("limit") Integer limit);
}

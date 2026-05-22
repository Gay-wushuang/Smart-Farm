package com.wisdom.farm.mapper;

import com.wisdom.farm.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderMapper {
    int insert(Order order);

    List<Order> selectList(@Param("userId") Long userId,
                           @Param("status") String status,
                           @Param("serviceType") String serviceType,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate);

    Order selectById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status,
                     @Param("statusText") String statusText,
                     @Param("acceptTime") java.time.LocalDateTime acceptTime);

    int complete(@Param("id") Long id,
                 @Param("status") Integer status,
                 @Param("statusText") String statusText,
                 @Param("completeTime") java.time.LocalDateTime completeTime,
                 @Param("completeImages") String completeImages,
                 @Param("completeRemark") String completeRemark);

    long count(@Param("status") String status,
               @Param("serviceType") String serviceType,
               @Param("startDate") LocalDate startDate,
               @Param("endDate") LocalDate endDate);
}

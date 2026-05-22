package com.wisdom.farm.service;

import com.wisdom.farm.entity.Order;
import com.wisdom.farm.dto.CompleteOrderRequest;
import com.wisdom.farm.dto.ServiceOrderRequest;
import com.wisdom.farm.vo.AdminStatsVO;
import com.wisdom.farm.vo.ClaimLandVO;
import com.wisdom.farm.vo.OrderCompleteVO;
import com.wisdom.farm.vo.OrderStatusVO;
import com.wisdom.farm.vo.OrderVO;
import com.wisdom.farm.vo.PageResult;

import java.util.List;

public interface OrderService {
    Order create(Long userId, Long landId);

    OrderVO createServiceOrder(Long userId, ServiceOrderRequest request);

    List<Order> list(Long userId);

    PageResult<OrderVO> listPage(Long userId, Integer page, Integer size, String status);

    PageResult<ClaimLandVO> claimLandPage(Long userId, Integer page, Integer size);

    Order getById(Long userId, Long id);

    OrderVO getDetail(Long userId, Long id);

    void cancel(Long userId, Long id);

    PageResult<OrderVO> adminList(Integer page, Integer size, String status, String serviceType, String startDate, String endDate);

    OrderStatusVO updateStatus(Long orderId, String status);

    OrderCompleteVO complete(Long orderId, CompleteOrderRequest request);

    AdminStatsVO adminStats();
}

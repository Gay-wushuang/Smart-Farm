package com.wisdom.farm.service.impl;

import com.wisdom.farm.dto.CompleteOrderRequest;
import com.wisdom.farm.dto.ServiceOrderRequest;
import com.wisdom.farm.entity.Land;
import com.wisdom.farm.entity.Order;
import com.wisdom.farm.enums.OrderStatus;
import com.wisdom.farm.enums.ServiceType;
import com.wisdom.farm.mapper.LandMapper;
import com.wisdom.farm.mapper.OrderMapper;
import com.wisdom.farm.mapper.PaymentMapper;
import com.wisdom.farm.mapper.UserMapper;
import com.wisdom.farm.service.OrderService;
import com.wisdom.farm.utils.ApiViewUtil;
import com.wisdom.farm.vo.AdminStatsVO;
import com.wisdom.farm.vo.ClaimLandVO;
import com.wisdom.farm.vo.OrderCompleteVO;
import com.wisdom.farm.vo.OrderStatusVO;
import com.wisdom.farm.vo.OrderVO;
import com.wisdom.farm.vo.PageResult;
import com.wisdom.farm.vo.ServiceTypeStatsVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final LandMapper landMapper;
    private final PaymentMapper paymentMapper;
    private final UserMapper userMapper;

    public OrderServiceImpl(OrderMapper orderMapper, LandMapper landMapper, PaymentMapper paymentMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.landMapper = landMapper;
        this.paymentMapper = paymentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Order create(Long userId, Long landId) {
        if (landId == null) {
            throw new IllegalArgumentException("土地ID不能为空");
        }
        Land land = landMapper.selectById(landId);
        if (land == null) {
            throw new IllegalArgumentException("土地不存在");
        }
        if (land.getStatus() != null && land.getStatus() != 1) {
            throw new IllegalArgumentException("土地当前不可认领");
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setLandId(landId);
        order.setAmount(land.getPrice());
        order.setStatus(0);
        order.setStatusText(OrderStatus.PENDING_PAY.name());
        order.setQuantity(1);
        order.setRemark("");
        order.setStartTime(LocalDateTime.now());
        order.setEndTime(LocalDateTime.now().plusYears(1));
        orderMapper.insert(order);
        return order;
    }

    @Override
    public OrderVO createServiceOrder(Long userId, ServiceOrderRequest request) {
        Long landId = request.getLandId();
        ServiceType serviceType = ServiceType.parse(request.getServiceType());
        Integer quantity = request.getQuantity() == null ? 1 : request.getQuantity();
        Land land = landMapper.selectById(landId);
        if (land == null) {
            throw new IllegalArgumentException("土地不存在");
        }
        BigDecimal unitPrice = serviceType == ServiceType.FERTILIZE ? BigDecimal.valueOf(35) : BigDecimal.valueOf(15);
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setLandId(landId);
        order.setAmount(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        order.setStatus(0);
        order.setStatusText(OrderStatus.PENDING_PAY.name());
        order.setServiceType(serviceType.name());
        order.setQuantity(quantity);
        order.setRemark(request.getRemark() == null ? "" : request.getRemark());
        orderMapper.insert(order);
        return ApiViewUtil.orderDetail(order, land);
    }

    @Override
    public List<Order> list(Long userId) {
        return orderMapper.selectList(userId, null, null, null, null);
    }

    @Override
    public PageResult<OrderVO> listPage(Long userId, Integer page, Integer size, String status) {
        String apiStatus = normalizeStatus(status);
        List<OrderVO> list = orderMapper.selectList(userId, apiStatus, null, null, null).stream()
                .map(order -> ApiViewUtil.orderDetail(order, landMapper.selectById(order.getLandId())))
                .toList();
        return ApiViewUtil.page(list, list.size(), page, size);
    }

    @Override
    public PageResult<ClaimLandVO> claimLandPage(Long userId, Integer page, Integer size) {
        List<ClaimLandVO> list = orderMapper.selectList(userId, null, null, null, null).stream()
                .map(order -> ApiViewUtil.claimLandItem(order, landMapper.selectById(order.getLandId())))
                .toList();
        return ApiViewUtil.page(list, list.size(), page, size);
    }

    @Override
    public Order getById(Long userId, Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null || !userId.equals(order.getUserId())) {
            return null;
        }
        return order;
    }

    @Override
    public OrderVO getDetail(Long userId, Long id) {
        Order order = getById(userId, id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        return ApiViewUtil.orderDetail(order, landMapper.selectById(order.getLandId()));
    }

    @Override
    public void cancel(Long userId, Long id) {
        Order order = getById(userId, id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        orderMapper.updateStatus(id, 2, OrderStatus.CANCELLED.name(), null);
    }

    @Override
    public PageResult<OrderVO> adminList(Integer page, Integer size, String status, String serviceType, String startDate, String endDate) {
        String apiStatus = normalizeStatus(status);
        String apiServiceType = normalizeServiceType(serviceType);
        LocalDate begin = parseDate(startDate);
        LocalDate finish = parseDate(endDate);
        List<OrderVO> list = orderMapper.selectList(null, apiStatus, apiServiceType, begin, finish).stream()
                .map(order -> ApiViewUtil.orderDetail(order, landMapper.selectById(order.getLandId())))
                .toList();
        return ApiViewUtil.page(list, orderMapper.count(apiStatus, apiServiceType, begin, finish), page, size);
    }

    @Override
    public OrderStatusVO updateStatus(Long orderId, String status) {
        OrderStatus orderStatus = OrderStatus.parse(status);
        int legacyStatus = orderStatus == OrderStatus.CANCELLED ? 2 : orderStatus == OrderStatus.PENDING_PAY ? 0 : 1;
        orderMapper.updateStatus(orderId, legacyStatus, orderStatus.name(), orderStatus == OrderStatus.PROCESSING ? LocalDateTime.now() : null);
        Order order = orderMapper.selectById(orderId);
        return ApiViewUtil.orderStatusResult(order);
    }

    @Override
    public OrderCompleteVO complete(Long orderId, CompleteOrderRequest request) {
        List<String> completeImages = request == null || request.getCompleteImages() == null ? List.of() : request.getCompleteImages();
        String completeRemark = request == null || request.getCompleteRemark() == null ? "" : request.getCompleteRemark();
        orderMapper.complete(orderId, 1, OrderStatus.COMPLETED.name(), LocalDateTime.now(), String.join(",", completeImages), completeRemark);
        Order order = orderMapper.selectById(orderId);
        return ApiViewUtil.orderCompleteResult(order);
    }

    @Override
    public AdminStatsVO adminStats() {
        ServiceTypeStatsVO serviceTypeStats = new ServiceTypeStatsVO();
        serviceTypeStats.setWaterCount(orderMapper.count(null, ServiceType.WATER.name(), null, null));
        serviceTypeStats.setFertilizeCount(orderMapper.count(null, ServiceType.FERTILIZE.name(), null, null));

        AdminStatsVO stats = new AdminStatsVO();
        stats.setTotalLands(landMapper.count(null, null));
        stats.setClaimedLands(landMapper.count(null, 2));
        stats.setTotalUsers(userMapper.count());
        stats.setTotalOrders(orderMapper.count(null, null, null, null));
        stats.setPendingOrders(orderMapper.count(OrderStatus.PENDING_PAY.name(), null, null, null));
        stats.setTodayRevenue(paymentMapper.todayRevenue());
        stats.setMonthRevenue(paymentMapper.monthRevenue());
        stats.setServiceTypeStats(serviceTypeStats);
        return stats;
    }

    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int suffix = (int) (Math.random() * 9000) + 1000;
        return "WF" + time + suffix;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return OrderStatus.parse(status).name();
    }

    private String normalizeServiceType(String serviceType) {
        if (serviceType == null || serviceType.isBlank()) {
            return null;
        }
        return ServiceType.parse(serviceType).name();
    }

    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return LocalDate.parse(date);
    }
}

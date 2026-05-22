package com.wisdom.farm.utils;

import com.wisdom.farm.entity.Land;
import com.wisdom.farm.entity.MonitorData;
import com.wisdom.farm.entity.Notification;
import com.wisdom.farm.entity.Order;
import com.wisdom.farm.entity.Payment;
import com.wisdom.farm.entity.User;
import com.wisdom.farm.enums.LandStatus;
import com.wisdom.farm.enums.OrderStatus;
import com.wisdom.farm.enums.ServiceType;
import com.wisdom.farm.vo.ClaimLandVO;
import com.wisdom.farm.vo.LandVO;
import com.wisdom.farm.vo.MonitorDataVO;
import com.wisdom.farm.vo.NotificationVO;
import com.wisdom.farm.vo.OperatorVO;
import com.wisdom.farm.vo.OrderCompleteVO;
import com.wisdom.farm.vo.OrderStatusVO;
import com.wisdom.farm.vo.OrderVO;
import com.wisdom.farm.vo.PageResult;
import com.wisdom.farm.vo.PaymentCreateVO;
import com.wisdom.farm.vo.PaymentStatusVO;
import com.wisdom.farm.vo.ServicePriceVO;
import com.wisdom.farm.vo.UserInfoVO;
import com.wisdom.farm.vo.WechatPayParamsVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public final class ApiViewUtil {
    private ApiViewUtil() {
    }

    public static <T> PageResult<T> page(List<T> list, long total, Integer page, Integer size) {
        PageResult<T> data = new PageResult<>();
        data.setList(list);
        data.setTotal(total);
        data.setPage(page == null || page < 1 ? 1 : page);
        data.setSize(size == null || size < 1 ? 10 : size);
        return data;
    }

    public static LandVO landItem(Land land) {
        LandVO data = new LandVO();
        data.setLandId(land.getId());
        data.setLandName(land.getName());
        data.setZone(land.getLocation());
        data.setArea(land.getArea());
        data.setAreaUnit("㎡");
        data.setImage(firstImage(land.getImages()));
        LandStatus status = LandStatus.fromDb(land.getStatus());
        data.setStatus(status.name());
        data.setStatusName(status.label());
        data.setPrice(land.getPrice());
        data.setPriceUnit("元/年");
        data.setSoilType("");
        data.setCropType("");
        data.setClaimUser(null);
        data.setClaimExpireTime(null);
        return data;
    }

    public static LandVO landDetail(Land land) {
        LandVO data = landItem(land);
        data.setImages(splitImages(land.getImages()));
        data.setDescription(land.getDescription());
        data.setClaimUser(null);
        data.setClaimTime(null);
        data.setLatestMonitor(monitorLatest());
        ServicePriceVO servicePrice = new ServicePriceVO();
        servicePrice.setWaterPrice(BigDecimal.valueOf(15));
        servicePrice.setFertilizePrice(BigDecimal.valueOf(35));
        servicePrice.setPriceUnit("元/次");
        data.setServicePrice(servicePrice);
        return data;
    }

    public static ClaimLandVO claimLandItem(Order order, Land land) {
        ClaimLandVO data = new ClaimLandVO();
        data.setClaimId(order.getId());
        data.setLandId(order.getLandId());
        data.setLandName(land == null ? "" : land.getName());
        data.setLandImage(land == null ? "" : firstImage(land.getImages()));
        data.setArea(land == null ? null : land.getArea());
        data.setAreaUnit("㎡");
        data.setClaimTime(order.getCreateTime());
        data.setExpireTime(order.getEndTime());
        data.setStatus(normalizeOrderStatus(order).name());
        return data;
    }

    public static OrderVO orderDetail(Order order, Land land) {
        OrderVO data = new OrderVO();
        data.setOrderId(order.getId());
        data.setOrderNo(order.getOrderNo());
        data.setUserId(order.getUserId());
        data.setLandId(order.getLandId());
        data.setLandName(land == null ? "" : land.getName());
        ServiceType serviceType = parseServiceType(order.getServiceType());
        data.setServiceType(serviceType.name());
        data.setServiceTypeName(serviceType.label());
        data.setQuantity(order.getQuantity() == null ? 1 : order.getQuantity());
        data.setAmount(order.getAmount());
        data.setRemark(order.getRemark() == null ? "" : order.getRemark());
        OrderStatus status = normalizeOrderStatus(order);
        data.setStatus(status.name());
        data.setStatusName(status.label());
        data.setCreateTime(order.getCreateTime());
        data.setPayTime(order.getPayTime());
        data.setStartTime(order.getStartTime());
        data.setEndTime(order.getEndTime());
        data.setUpdateTime(order.getUpdateTime());
        data.setAcceptTime(order.getAcceptTime());
        data.setCompleteTime(order.getCompleteTime());
        data.setOperator(operator(order));
        data.setCompleteImages(splitImages(order.getCompleteImages()));
        data.setCompleteRemark(order.getCompleteRemark() == null ? "" : order.getCompleteRemark());
        return data;
    }

    public static OrderStatusVO orderStatusResult(Order order) {
        OrderStatusVO data = new OrderStatusVO();
        OrderStatus status = normalizeOrderStatus(order);
        data.setOrderId(order.getId());
        data.setStatus(status.name());
        data.setStatusName(status.label());
        data.setAcceptTime(order.getAcceptTime());
        return data;
    }

    public static OrderCompleteVO orderCompleteResult(Order order) {
        OrderCompleteVO data = new OrderCompleteVO();
        data.setOrderId(order.getId());
        data.setStatus(OrderStatus.COMPLETED.name());
        data.setStatusName(OrderStatus.COMPLETED.label());
        data.setCompleteTime(order.getCompleteTime());
        data.setCompleteImages(splitImages(order.getCompleteImages()));
        data.setCompleteRemark(order.getCompleteRemark() == null ? "" : order.getCompleteRemark());
        return data;
    }

    public static MonitorDataVO monitorLatest() {
        MonitorDataVO data = new MonitorDataVO();
        data.setTemperature(BigDecimal.valueOf(25.0));
        data.setHumidity(BigDecimal.valueOf(60.0));
        data.setSoilMoisture(BigDecimal.valueOf(45.0));
        data.setLightIntensity(BigDecimal.valueOf(1200.0));
        data.setUpdateTime(LocalDateTime.now());
        return data;
    }

    public static MonitorDataVO monitorLatest(MonitorData monitor) {
        if (monitor == null) {
            return monitorLatest();
        }
        MonitorDataVO data = new MonitorDataVO();
        data.setTemperature(monitor.getTemperature());
        data.setHumidity(monitor.getHumidity());
        data.setSoilMoisture(monitor.getSoilMoisture());
        data.setLightIntensity(monitor.getLightIntensity());
        data.setUpdateTime(monitor.getCreateTime());
        return data;
    }

    public static PaymentCreateVO paymentResult(Payment payment, WechatPayParamsVO wxPayParams) {
        PaymentCreateVO data = new PaymentCreateVO();
        data.setPaymentId(payment.getId());
        data.setPaymentNo(payment.getPaymentNo());
        data.setAmount(payment.getAmount());
        data.setWxPayParams(wxPayParams);
        return data;
    }

    public static PaymentStatusVO paymentStatus(Payment payment) {
        PaymentStatusVO data = new PaymentStatusVO();
        data.setPaymentId(payment.getId());
        data.setPaymentNo(payment.getPaymentNo());
        data.setAmount(payment.getAmount());
        data.setStatus(payment.getStatus());
        data.setStatusName(paymentStatusName(payment.getStatus()));
        data.setPayTime(payment.getPayTime());
        data.setPayMethod(payment.getPayMethod());
        return data;
    }

    public static NotificationVO notificationItem(Notification notification) {
        NotificationVO data = new NotificationVO();
        data.setNotificationId(notification.getId());
        data.setTitle(notification.getTitle());
        data.setContent(notification.getContent());
        data.setType(notification.getType());
        data.setIsRead(Boolean.TRUE.equals(notification.getRead()));
        data.setCreateTime(notification.getCreateTime());
        data.setExtra(notification.getExtra() == null ? "{}" : notification.getExtra());
        return data;
    }

    public static UserInfoVO userInfo(User user) {
        UserInfoVO data = new UserInfoVO();
        if (user == null) {
            return data;
        }
        data.setUserId(user.getId());
        data.setNickname(user.getNickname());
        data.setAvatar(user.getAvatar());
        data.setPhone(user.getPhone());
        return data;
    }

    public static BigDecimal amountFromLand(Land land, Integer duration) {
        if (land == null || land.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        int months = duration == null || duration < 1 ? 12 : duration;
        return land.getPrice().multiply(BigDecimal.valueOf(months)).divide(BigDecimal.valueOf(12), 2, java.math.RoundingMode.HALF_UP);
    }

    private static OperatorVO operator(Order order) {
        if (order.getOperatorId() == null) {
            return null;
        }
        OperatorVO operator = new OperatorVO();
        operator.setAdminId(order.getOperatorId());
        operator.setAdminName(order.getOperatorName() == null ? "" : order.getOperatorName());
        return operator;
    }

    private static String firstImage(String images) {
        List<String> list = splitImages(images);
        return list.isEmpty() ? "" : list.get(0);
    }

    private static List<String> splitImages(String images) {
        if (images == null || images.isBlank()) {
            return List.of();
        }
        return Arrays.stream(images.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    private static OrderStatus normalizeOrderStatus(Order order) {
        if (order.getStatusText() != null && !order.getStatusText().isBlank()) {
            return OrderStatus.parse(order.getStatusText());
        }
        return OrderStatus.fromDb(order.getStatus());
    }

    private static ServiceType parseServiceType(String serviceType) {
        try {
            return ServiceType.parse(serviceType);
        } catch (IllegalArgumentException e) {
            return ServiceType.WATER;
        }
    }

    private static String paymentStatusName(String status) {
        if ("SUCCESS".equalsIgnoreCase(status)) {
            return "支付成功";
        }
        if ("FAILED".equalsIgnoreCase(status)) {
            return "支付失败";
        }
        return "待支付";
    }
}

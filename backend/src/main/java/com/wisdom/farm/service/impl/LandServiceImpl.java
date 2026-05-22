package com.wisdom.farm.service.impl;

import com.wisdom.farm.dto.LandUpsertRequest;
import com.wisdom.farm.entity.Land;
import com.wisdom.farm.entity.Order;
import com.wisdom.farm.enums.ClaimDuration;
import com.wisdom.farm.enums.OrderStatus;
import com.wisdom.farm.mapper.LandMapper;
import com.wisdom.farm.mapper.MonitorDataMapper;
import com.wisdom.farm.mapper.OrderMapper;
import com.wisdom.farm.service.LandService;
import com.wisdom.farm.utils.ApiViewUtil;
import com.wisdom.farm.vo.ClaimResultVO;
import com.wisdom.farm.vo.LandMonitorVO;
import com.wisdom.farm.vo.LandVO;
import com.wisdom.farm.vo.PageResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LandServiceImpl implements LandService {
    private final LandMapper landMapper;
    private final OrderMapper orderMapper;
    private final MonitorDataMapper monitorDataMapper;

    public LandServiceImpl(LandMapper landMapper, OrderMapper orderMapper, MonitorDataMapper monitorDataMapper) {
        this.landMapper = landMapper;
        this.orderMapper = orderMapper;
        this.monitorDataMapper = monitorDataMapper;
    }

    @Override
    public List<Land> list(Integer page, Integer pageSize) {
        int currentPage = page == null || page < 1 ? 1 : page;
        int size = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);
        int offset = (currentPage - 1) * size;
        return landMapper.selectList(offset, size, null, null);
    }

    @Override
    public Land getById(Long id) {
        return landMapper.selectById(id);
    }

    @Override
    public PageResult<LandVO> listPage(Integer page, Integer size, String zone, String status) {
        int currentPage = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        Integer dbStatus = landStatusToDb(status);
        List<LandVO> list = landMapper.selectList((currentPage - 1) * pageSize, pageSize, zone, dbStatus)
                .stream()
                .map(ApiViewUtil::landItem)
                .toList();
        return ApiViewUtil.page(list, landMapper.count(zone, dbStatus), page, size);
    }

    @Override
    public LandVO getDetail(Long id) {
        Land land = landMapper.selectById(id);
        if (land == null) {
            throw new IllegalArgumentException("土地不存在");
        }
        return ApiViewUtil.landDetail(land);
    }

    @Override
    public ClaimResultVO claim(Long userId, Long landId, Integer duration) {
        int months = ClaimDuration.normalize(duration);
        Land land = landMapper.selectById(landId);
        if (land == null) {
            throw new IllegalArgumentException("土地不存在");
        }
        if (land.getStatus() != null && land.getStatus() == 2) {
            throw new IllegalArgumentException("土地已被认领");
        }
        Order order = new Order();
        order.setOrderNo("CL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int) (Math.random() * 9000 + 1000));
        order.setUserId(userId);
        order.setLandId(landId);
        order.setAmount(ApiViewUtil.amountFromLand(land, months));
        order.setStatus(0);
        order.setStatusText(OrderStatus.PENDING_PAY.name());
        order.setQuantity(1);
        order.setRemark("");
        order.setStartTime(LocalDateTime.now());
        order.setEndTime(LocalDateTime.now().plusMonths(months));
        orderMapper.insert(order);
        landMapper.updateStatus(landId, 2);
        ClaimResultVO result = new ClaimResultVO();
        result.setClaimId(order.getId());
        result.setLandId(landId);
        result.setLandName(land.getName());
        result.setDuration(months);
        result.setAmount(order.getAmount());
        result.setPaymentNo(order.getOrderNo());
        result.setStatus(OrderStatus.PENDING_PAY.name());
        result.setExpireTime(LocalDateTime.now().plusMinutes(30));
        return result;
    }

    @Override
    public LandMonitorVO monitor(Long landId, String range) {
        Land land = landMapper.selectById(landId);
        if (land == null) {
            throw new IllegalArgumentException("土地不存在");
        }
        java.time.LocalDateTime since = switch (range == null ? "24h" : range) {
            case "7d" -> LocalDateTime.now().minusDays(7);
            case "30d" -> LocalDateTime.now().minusDays(30);
            default -> LocalDateTime.now().minusHours(24);
        };
        LandMonitorVO result = new LandMonitorVO();
        result.setLandId(landId);
        result.setLandName(land.getName());
        result.setLatest(ApiViewUtil.monitorLatest(monitorDataMapper.latest(landId)));
        result.setHistory(monitorDataMapper.history(landId, since).stream().map(ApiViewUtil::monitorLatest).toList());
        return result;
    }

    @Override
    public LandVO create(LandUpsertRequest request) {
        Land land = fromRequest(new Land(), request);
        land.setStatus(1);
        landMapper.insert(land);
        return ApiViewUtil.landDetail(land);
    }

    @Override
    public LandVO update(Long landId, LandUpsertRequest request) {
        Land land = landMapper.selectById(landId);
        if (land == null) {
            throw new IllegalArgumentException("土地不存在");
        }
        fromRequest(land, request);
        landMapper.update(land);
        return ApiViewUtil.landDetail(land);
    }

    @Override
    public void release(Long landId) {
        landMapper.updateStatus(landId, 1);
    }

    private Land fromRequest(Land land, LandUpsertRequest request) {
        land.setName(stringValue(request.getLandName()));
        land.setLocation(stringValue(request.getZone()));
        land.setArea(request.getArea() == null ? BigDecimal.ZERO : request.getArea());
        land.setPrice(request.getPrice() == null ? BigDecimal.ZERO : request.getPrice());
        land.setDescription(stringValue(request.getDescription()));
        land.setImages(request.getImages() == null ? "" : String.join(",", request.getImages()));
        if (land.getStatus() == null) {
            land.setStatus(1);
        }
        return land;
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private Integer landStatusToDb(String status) {
        if (status == null || status.isBlank() || "ALL".equalsIgnoreCase(status)) {
            return null;
        }
        if ("CLAIMED".equalsIgnoreCase(status)) {
            return 2;
        }
        return 1;
    }
}

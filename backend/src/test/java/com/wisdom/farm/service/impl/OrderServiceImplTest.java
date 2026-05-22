package com.wisdom.farm.service.impl;

import com.wisdom.farm.entity.Land;
import com.wisdom.farm.entity.Order;
import com.wisdom.farm.mapper.LandMapper;
import com.wisdom.farm.mapper.OrderMapper;
import com.wisdom.farm.mapper.PaymentMapper;
import com.wisdom.farm.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {
    @Test
    void createShouldBuildPendingOrderFromLand() {
        OrderMapper orderMapper = mock(OrderMapper.class);
        LandMapper landMapper = mock(LandMapper.class);
        Land land = new Land();
        land.setId(3L);
        land.setPrice(new BigDecimal("299.00"));
        land.setStatus(1);
        when(landMapper.selectById(3L)).thenReturn(land);

        OrderServiceImpl service = new OrderServiceImpl(orderMapper, landMapper, mock(PaymentMapper.class), mock(UserMapper.class));
        Order result = service.create(9L, 3L);

        assertEquals(9L, result.getUserId());
        assertEquals(3L, result.getLandId());
        assertEquals(new BigDecimal("299.00"), result.getAmount());
        assertEquals(0, result.getStatus());

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).insert(captor.capture());
        assertEquals(3L, captor.getValue().getLandId());
    }

    @Test
    void createShouldRejectUnavailableLand() {
        OrderMapper orderMapper = mock(OrderMapper.class);
        LandMapper landMapper = mock(LandMapper.class);
        Land land = new Land();
        land.setStatus(2);
        when(landMapper.selectById(3L)).thenReturn(land);

        OrderServiceImpl service = new OrderServiceImpl(orderMapper, landMapper, mock(PaymentMapper.class), mock(UserMapper.class));

        assertThrows(IllegalArgumentException.class, () -> service.create(9L, 3L));
    }
}

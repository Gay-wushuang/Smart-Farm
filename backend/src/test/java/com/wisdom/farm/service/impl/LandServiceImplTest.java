package com.wisdom.farm.service.impl;

import com.wisdom.farm.entity.Land;
import com.wisdom.farm.mapper.LandMapper;
import com.wisdom.farm.mapper.MonitorDataMapper;
import com.wisdom.farm.mapper.OrderMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LandServiceImplTest {
    @Test
    void listShouldNormalizePagination() {
        LandMapper landMapper = mock(LandMapper.class);
        Land land = new Land();
        when(landMapper.selectList(0, 10, null, null)).thenReturn(List.of(land));

        LandServiceImpl service = new LandServiceImpl(landMapper, mock(OrderMapper.class), mock(MonitorDataMapper.class));
        List<Land> result = service.list(null, null);

        assertEquals(1, result.size());
        verify(landMapper).selectList(0, 10, null, null);
    }

    @Test
    void listShouldLimitPageSize() {
        LandMapper landMapper = mock(LandMapper.class);
        when(landMapper.selectList(100, 100, null, null)).thenReturn(List.of());

        LandServiceImpl service = new LandServiceImpl(landMapper, mock(OrderMapper.class), mock(MonitorDataMapper.class));
        service.list(2, 200);

        verify(landMapper).selectList(100, 100, null, null);
    }
}

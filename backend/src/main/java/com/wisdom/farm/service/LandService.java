package com.wisdom.farm.service;

import com.wisdom.farm.entity.Land;
import com.wisdom.farm.dto.LandUpsertRequest;
import com.wisdom.farm.vo.ClaimResultVO;
import com.wisdom.farm.vo.LandMonitorVO;
import com.wisdom.farm.vo.LandVO;
import com.wisdom.farm.vo.PageResult;

import java.util.List;

public interface LandService {
    List<Land> list(Integer page, Integer pageSize);

    Land getById(Long id);

    PageResult<LandVO> listPage(Integer page, Integer size, String zone, String status);

    LandVO getDetail(Long id);

    ClaimResultVO claim(Long userId, Long landId, Integer duration);

    LandMonitorVO monitor(Long landId, String range);

    LandVO create(LandUpsertRequest request);

    LandVO update(Long landId, LandUpsertRequest request);

    void release(Long landId);
}

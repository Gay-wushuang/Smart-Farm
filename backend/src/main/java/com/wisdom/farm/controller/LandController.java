package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.dto.ClaimLandRequest;
import com.wisdom.farm.service.LandService;
import com.wisdom.farm.vo.ClaimResultVO;
import com.wisdom.farm.vo.LandMonitorVO;
import com.wisdom.farm.vo.LandVO;
import com.wisdom.farm.vo.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "土地管理", description = "土地相关接口")
@RestController
@RequestMapping("/lands")
public class LandController {
    private final LandService landService;

    public LandController(LandService landService) {
        this.landService = landService;
    }

    @Operation(summary = "获取土地列表")
    @GetMapping
    public Result<PageResult<LandVO>> getLandList(@RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) String zone,
                                                  @RequestParam(required = false) String status) {
        return Result.success(landService.listPage(page, size, zone, status));
    }

    @Operation(summary = "获取土地详情")
    @GetMapping("/{landId}")
    public Result<LandVO> getLandDetail(@PathVariable Long landId) {
        try {
            return Result.success(landService.getDetail(landId));
        } catch (IllegalArgumentException e) {
            return Result.error("土地不存在");
        }
    }

    @Operation(summary = "认领土地")
    @PostMapping("/{landId}/claim")
    public Result<ClaimResultVO> claimLand(@PathVariable Long landId, @RequestBody ClaimLandRequest request) {
        try {
            return Result.success(landService.claim(UserContext.requireUserId(), landId, request.getDuration()));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取土地监测数据")
    @GetMapping("/{landId}/monitor")
    public Result<LandMonitorVO> getMonitor(@PathVariable Long landId,
                                            @RequestParam(required = false) String range) {
        try {
            return Result.success(landService.monitor(landId, range));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
}

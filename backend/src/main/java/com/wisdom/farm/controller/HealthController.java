package com.wisdom.farm.controller;

import com.wisdom.farm.common.Result;
import com.wisdom.farm.vo.HealthVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/")
    public Result<HealthVO> index() {
        HealthVO health = new HealthVO();
        health.setName("wisdom-farm");
        health.setStatus("running");
        health.setSwagger("/swagger-ui.html");
        health.setApiDocs("/v3/api-docs");
        return Result.success(health);
    }
}

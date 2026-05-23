package com.wisdom.farm.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class HandleAlertRequest {
    @JsonAlias("handleRemark")
    private String remark;
}

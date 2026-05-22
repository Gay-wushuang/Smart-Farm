package com.wisdom.farm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminStatsVO {
    private Long totalLands;
    private Long claimedLands;
    private Long totalUsers;
    private Long totalOrders;
    private Long pendingOrders;
    private BigDecimal todayRevenue;
    private BigDecimal monthRevenue;
    private ServiceTypeStatsVO serviceTypeStats;
}

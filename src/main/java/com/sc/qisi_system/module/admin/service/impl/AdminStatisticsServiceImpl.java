package com.sc.qisi_system.module.admin.service.impl;

import com.sc.qisi_system.module.admin.dto.DateQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminStatisticsService;
import com.sc.qisi_system.module.admin.vo.AdminStatVO;
import com.sc.qisi_system.module.admin.vo.DemandCategoryVO;
import com.sc.qisi_system.module.admin.vo.DemandDailyTrendVO;
import com.sc.qisi_system.module.demand.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {


    private final DemandService demandService;


    @Override
    public AdminStatVO getAdminDataStatistics() {
        return demandService.getAdminDataStatistics();
    }


    @Override
    public List<DemandDailyTrendVO> getDemandTrend(DateQueryDTO dateQueryDTO) {
        return demandService.selectDemandTrendByDate(dateQueryDTO);
    }


    @Override
    public List<DemandCategoryVO> getDemandCategoryDistribution() {
        return demandService.getDemandCategoryDistribution();
    }
}

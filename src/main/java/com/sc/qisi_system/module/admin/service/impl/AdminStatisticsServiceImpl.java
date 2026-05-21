package com.sc.qisi_system.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.module.admin.service.AdminStatisticsService;
import com.sc.qisi_system.module.admin.vo.StatusDataVO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {


    private final DemandService demandService;


    /**
     * 查询已发布需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    @Override
    public Long getPublishedDemandCount() {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getStatus, DemandStatusEnum.PUBLISHED.getCode());
        return demandService.count(wrapper);
    }


    /**
     * 查询研究中需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    @Override
    public Long getResearchingDemandCount() {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getStatus,DemandStatusEnum.RESEARCHING.getCode());
        return demandService.count(wrapper);
    }


    /**
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    @Override
    public Long getPendingReviewDemandCount() {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getStatus,DemandStatusEnum.REVIEWING.getCode());
        return demandService.count(wrapper);
    }


    /**
     * 查询需求状态分布统计接口
     *
     * @author 郭双祎
     */
    @Override
    public StatusDataVO getDemandStatusDistribution() {
        //1.定义需求状态和接口数量的集合
        List<String> statusList = new ArrayList<>();
        List<Long> numberList = new ArrayList<>();
        //2.遍历查询所有状态接口的数量
        for(DemandStatusEnum status :DemandStatusEnum.values()){
            LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Demand::getStatus,status.getCode());
            Long count = demandService.count(wrapper);
            statusList.add(status.getDesc());
            numberList.add(count);
        }
        //3.封装查询结果
        StatusDataVO statusDataVO = new StatusDataVO();
        statusDataVO.setStatusList(statusList);
        statusDataVO.setNumberList(numberList);
        return statusDataVO;
    }
}

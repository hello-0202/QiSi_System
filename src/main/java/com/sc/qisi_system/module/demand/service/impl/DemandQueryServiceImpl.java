package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.vo.*;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


/**
 * 需求查询服务实现类
 */
@RequiredArgsConstructor
@Service
public class DemandQueryServiceImpl implements DemandQueryService {


    private final DemandMapper demandMapper;
    private final DemandService demandService;
    private final SysUserService sysUserService;


    /**
     * 获取我的草稿列表
     */
    @Override
    public PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize) {
        // 1. 构建分页对象
        Page<Demand> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件：查询我的草稿
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Demand::getPublisherId, userId)
                .eq(Demand::getStatus, DemandStatusEnum.DRAFT.getCode())
                .orderByDesc(Demand::getCreateTime);

        // 3. 分页查询数据
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        // 4. 转换并返回结果
        return demandService.convertToMyPageResultList(demandIPage);
    }


    /**
     * 获取我的需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {
        return demandService.getMyDemandList(userId, myDemandQueryDTO);
    }


    /**
     * 获取我的需求详情
     */
    @Override
    public MyDemandDetailVO getMyDemandDetail(Long demandId) {
        // 1. 查询需求基础信息
        Demand demand = demandService.getDemand(demandId);

        // 2. 转换为VO对象
        MyDemandDetailVO myDemandDetailVO = new MyDemandDetailVO();
        BeanUtils.copyProperties(demand, myDemandDetailVO);

        // 3. 复制发布者信息
        BeanUtils.copyProperties(sysUserService.getUserProfile(demand.getPublisherId()), myDemandDetailVO.getDemandPublisherDetailVO());

        return myDemandDetailVO;
    }


    /**
     * 获取公开需求详情
     */
    @Override
    public DemandPublicDetailVO getPublicDemandDetail(Long demandId) {
        return demandService.getPublicDemandDetail(demandId);
    }
}
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


@RequiredArgsConstructor
@Service
public class DemandQueryServiceImpl implements DemandQueryService {


    private final DemandMapper demandMapper;
    private final DemandService demandService;
    private final SysUserService sysUserService;


    @Override
    public PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize) {

        Page<Demand> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
                .eq(Demand::getPublisherId, userId)
                .eq(Demand::getStatus, DemandStatusEnum.DRAFT.getCode())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        return demandService.convertToMyPageResultList(demandIPage);
    }


    @Override
    public PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {
        return demandService.getMyDemandList(userId, myDemandQueryDTO);
    }


    @Override
    public MyDemandDetailVO getMyDemandDetail(Long demandId) {

        Demand demand = demandService.getDemand(demandId);

        MyDemandDetailVO myDemandDetailVO = new MyDemandDetailVO();
        BeanUtils.copyProperties(demand, myDemandDetailVO);
        BeanUtils.copyProperties(sysUserService.getUserProfile(demand.getPublisherId()), myDemandDetailVO.getDemandPublisherDetailVO());

        return myDemandDetailVO;
    }


    @Override
    public DemandPublicDetailVO getPublicDemandDetail(Long demandId) {
        return demandService.getPublicDemandDetail(demandId);
    }
}




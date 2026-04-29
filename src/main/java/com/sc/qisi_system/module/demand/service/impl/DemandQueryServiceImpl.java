package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.demand.domain.DemandPracticeList;
import com.sc.qisi_system.module.user.domain.UserInfoBase;
import com.sc.qisi_system.module.demand.domain.DemandPublisherList;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.vo.*;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DemandQueryServiceImpl implements DemandQueryService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final DemandMemberService demandMemberService;


    @Override
    public PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize) {

        Page<Demand> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
                .eq(Demand::getPublisherId, userId)
                .eq(Demand::getStatus, DemandStatusEnum.DRAFT.getCode())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        return convertToMyPageResultList(demandIPage);
    }


    @Override
    public PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {

        Page<Demand> page = new Page<>(myDemandQueryDTO.getPageNum(), myDemandQueryDTO.getPageSize());
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Demand::getPublisherId, userId)
                .gt(Demand::getStatus, DemandStatusEnum.DRAFT.getCode());

        if (myDemandQueryDTO.getCreateTime() != null) {
            queryWrapper.ge(Demand::getCreateTime, myDemandQueryDTO.getCreateTime());
        }
        if (CollectionUtils.isNotEmpty(myDemandQueryDTO.getStatusList())) {
            queryWrapper.in(Demand::getStatus, myDemandQueryDTO.getStatusList());
        }
        queryWrapper.orderByDesc(Demand::getCreateTime)
                .orderByDesc(Demand::getProgressPercent);

        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        return convertToMyPageResultList(demandIPage);
    }


    @Override
    public MyDemandDetailVO getMyDemandDetail(Long demandId) {

        Demand demand = getDemand(demandId);

        MyDemandDetailVO myDemandDetailVO = new MyDemandDetailVO();
        BeanUtils.copyProperties(demand, myDemandDetailVO);
        BeanUtils.copyProperties(sysUserService.getDemandUserBase(demand.getPublisherId()), myDemandDetailVO.getDemandPublisherDetailVO());

        return myDemandDetailVO;
    }






    @Override
    public DemandReceiverDetailVO getDemandReceiverDetail(Long demandId) {
        Demand demand = getDemand(demandId);

        if (Objects.equals(demand.getStatus(), DemandStatusEnum.PUBLISHED.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_PUBLISHED);
        }

        DemandReceiverDetailVO demandReceiverDetailVO = new DemandReceiverDetailVO();
        BeanUtils.copyProperties(demand, demandReceiverDetailVO);
        BeanUtils.copyProperties(sysUserService.getDemandUserBase(demand.getPublisherId()), demandReceiverDetailVO.getDemandPublisherDetailVO());

        return demandReceiverDetailVO;
    }





    @Override
    public PageResult<DemandListVO> getMyPracticeDemandList(Long userId, PracticeDemandQueryDTO queryDTO) {

        // 1. 设置分页拆查询
        Page<Demand> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 设置查询条件
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .in(Demand::getStatus,queryDTO.getStatusList())
                .orderByDesc(Demand::getCreateTime);

        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        // 3. 返回包装
        return convertToPracticePageResultList(userId,demandIPage);
    }


    /**
     *
     */
    private Demand getDemand(Long demandId) {

        Demand demand = demandMapper.selectById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        return demand;
    }


    /**
     *
     */
    private PageResult<DemandListVO> convertToMyPageResultList(IPage<Demand> demandIPage) {

        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {

                    UserInfoBase userInfoBase = sysUserService.getDemandUserBase(demand.getPublisherId());
                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);
                    DemandPublisherList demandPublisherList = new DemandPublisherList();
                    BeanUtils.copyProperties(userInfoBase, demandPublisherList);
                    vo.setDemandPublisherList(demandPublisherList);

                    return vo;

                }).toList();

        PageResult<DemandListVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandIPage.getTotal());
        pageResult.setPages(demandIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }





    /**
     *
     */
    private PageResult<DemandListVO> convertToPracticePageResultList(Long userId,IPage<Demand> demandIPage) {

        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {

                    UserInfoBase userInfoBase = sysUserService.getDemandUserBase(demand.getPublisherId());
                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);
                    DemandPublisherList demandPublisherList = new DemandPublisherList();
                    BeanUtils.copyProperties(userInfoBase, demandPublisherList);
                    vo.setDemandPublisherList(demandPublisherList);

                    LambdaQueryWrapper<DemandMember> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper
                            .eq(DemandMember::getDemandId,demand.getId())
                            .eq(DemandMember::getUserId,userId);
                    DemandMember demandMember = demandMemberService.getOne(queryWrapper);
                    DemandPracticeList demandPracticeList = new DemandPracticeList();
                    if (demandMember != null) {
                        BeanUtils.copyProperties(demandMember, demandPracticeList);
                    }
                    vo.setDemandPracticeList(demandPracticeList);

                    return vo;

                }).toList();

        PageResult<DemandListVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandIPage.getTotal());
        pageResult.setPages(demandIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }
}

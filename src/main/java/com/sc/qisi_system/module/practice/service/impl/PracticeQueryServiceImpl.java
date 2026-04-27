package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import com.sc.qisi_system.module.apply.vo.ApplyMemberListVO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandReceiverDetailVO;
import com.sc.qisi_system.module.demand.vo.MyDemandDetailVO;
import com.sc.qisi_system.module.practice.dto.MemberChangeLogDTO;
import com.sc.qisi_system.module.practice.dto.QueryDemandProgressLogDTO;
import com.sc.qisi_system.module.practice.entity.DemandMemberChange;
import com.sc.qisi_system.module.practice.entity.DemandProgress;
import com.sc.qisi_system.module.practice.mapper.DemandMemberChangeMapper;
import com.sc.qisi_system.module.practice.mapper.DemandProgressMapper;
import com.sc.qisi_system.module.practice.service.PracticeQueryService;
import com.sc.qisi_system.module.practice.vo.DemandProgressVO;
import com.sc.qisi_system.module.practice.vo.MemberChangeLogVO;
import com.sc.qisi_system.module.user.domain.UserInfoBase;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PracticeQueryServiceImpl implements PracticeQueryService {


    private final DemandMemberChangeMapper demandMemberChangeMapper;
    private final DemandProgressMapper demandProgressMapper;
    private final DemandService demandService;
    private final DemandQueryService demandQueryService;
    private final ApplyQueryService applyQueryService;
    private final SysUserService sysUserService;


    @Override
    public PageResult<DemandListVO> getPracticeDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {

        List<Integer> allowedStatus = Arrays.asList(
                DemandStatusEnum.PUBLISHED.getCode(),
                DemandStatusEnum.RESEARCHING.getCode(),
                DemandStatusEnum.COMPLETED.getCode(),
                DemandStatusEnum.CLOSED.getCode()
        );

        List<Integer> statusList = Optional.ofNullable(myDemandQueryDTO.getStatusList()).orElse(new ArrayList<>());

        for (Integer status : statusList) {
            if (!allowedStatus.contains(status)) {
                throw new BusinessException(ResultCode.DEMAND_STATUS_ILLEGAL);
            }
        }

        if (statusList.isEmpty()) {
            myDemandQueryDTO.setStatusList(allowedStatus);
        }

        return demandQueryService.getMyDemandList(userId, myDemandQueryDTO);
    }


    @Override
    public MyDemandDetailVO getPracticeDemandDetail(Long demandId) {
        return demandQueryService.getMyDemandDetail(demandId);
    }


    @Override
    public PageResult<DemandListVO> getMyPracticeList(Long userId, PracticeDemandQueryDTO practiceDemandQueryDTO) {
        return demandQueryService.getMyPracticeDemandList(userId, practiceDemandQueryDTO);
    }


    @Override
    public DemandReceiverDetailVO getMyPracticeDetail(Long demandId) {
        return demandQueryService.getDemandReceiverDetail(demandId);
    }


    @Override
    public List<ApplyMemberListVO> getMemberList(Long userId, Long demandId) {
        return applyQueryService.getApplyMemberList(userId, demandId);
    }


    @Override
    public ApplyMemberListVO getMemberDetailInfo(Long userId) {
        return applyQueryService.getApplyMemberDetail(userId);
    }


    @Override
    public PageResult<MemberChangeLogVO> getDemandMemberChangeLog(MemberChangeLogDTO memberChangeLogDTO) {

        Page<DemandMemberChange> page = new Page<>(memberChangeLogDTO.getPageNum(), memberChangeLogDTO.getPageSize());

        if (!demandService.notExistsByDemandId(memberChangeLogDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        LambdaQueryWrapper<DemandMemberChange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandMemberChange::getDemandId, memberChangeLogDTO.getDemandId());
        IPage<DemandMemberChange> demandMemberChangeIPage = demandMemberChangeMapper.selectPage(page, queryWrapper);

        List<MemberChangeLogVO> voList = demandMemberChangeIPage.getRecords().stream().map(
                memberChange -> {

                    MemberChangeLogVO memberChangeLogVO = new MemberChangeLogVO();

                    BeanUtils.copyProperties(memberChange, memberChangeLogVO);
                    UserInfoBase userInfoBase = sysUserService.getDemandUserBase(memberChange.getUserId());
                    BeanUtils.copyProperties(userInfoBase, memberChangeLogVO.getUserInfoBase());
                    return memberChangeLogVO;

                }).toList();

        PageResult<MemberChangeLogVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandMemberChangeIPage.getTotal());
        pageResult.setPages(demandMemberChangeIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }


    @Override
    public PageResult<DemandProgressVO> getDemandProgressLog(QueryDemandProgressLogDTO queryDemandProgressLogDTO) {

        Page<DemandProgress> page = new Page<>(queryDemandProgressLogDTO.getPageNum(), queryDemandProgressLogDTO.getPageSize());

        if(demandService.notExistsByDemandId(queryDemandProgressLogDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        LambdaQueryWrapper<DemandProgress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandProgress::getDemandId, queryDemandProgressLogDTO.getDemandId());
        IPage<DemandProgress> demandProgressIPage = demandProgressMapper.selectPage(page, queryWrapper);

        List<DemandProgressVO> demandProgressVOList = demandProgressIPage.getRecords().stream().map(
                demandProgress -> {
                    DemandProgressVO demandProgressVO = new DemandProgressVO();
                    BeanUtils.copyProperties(demandProgress, demandProgressVO);

                    SysUser sysUser = sysUserService.getOne(Wrappers.lambdaQuery(SysUser.class)
                            .eq(SysUser::getId,demandProgress.getUserId())
                            .select(SysUser::getName));
                    if (sysUser != null) {
                        demandProgressVO.setName(sysUser.getName());
                    }

                    return demandProgressVO;
                }).toList();

        PageResult<DemandProgressVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandProgressIPage.getTotal());
        pageResult.setPages(demandProgressIPage.getPages());
        pageResult.setRecords(demandProgressVOList);

        return pageResult;
    }
}

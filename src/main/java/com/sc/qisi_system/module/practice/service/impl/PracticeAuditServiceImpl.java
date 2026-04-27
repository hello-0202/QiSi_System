package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.enums.MemberChangeStatusEnum;
import com.sc.qisi_system.common.enums.MemberChangeTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.mapper.DemandMemberMapper;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import com.sc.qisi_system.module.practice.entity.DemandExecutionPlan;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.entity.DemandMemberChange;
import com.sc.qisi_system.module.practice.mapper.DemandExecutionPlanMapper;
import com.sc.qisi_system.module.practice.mapper.DemandMemberChangeMapper;
import com.sc.qisi_system.module.practice.service.PracticeAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PracticeAuditServiceImpl implements PracticeAuditService {


    private final DemandMemberChangeMapper demandMemberChangeMapper;
    private final DemandMemberMapper demandMemberMapper;
    private final DemandExecutionPlanMapper demandExecutionPlanMapper;
    private final DemandService demandService;


    @Override
    public void submitDemandPlan(DemandPlanDTO demandPlanDTO) {
        if(demandService.notExistsByDemandId(demandPlanDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        DemandExecutionPlan executionPlan = new DemandExecutionPlan();
        BeanUtils.copyProperties(demandPlanDTO, executionPlan);
        executionPlan.setOperatorId(SecurityUtils.getCurrentUserId());

        demandExecutionPlanMapper.insert(executionPlan);
    }


    @Override
    public void startResearch(Long demandId) {
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.PUBLISHED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }
        demand.setStatus(DemandStatusEnum.RESEARCHING.getCode());
        demandService.updateById(demand);
    }


    @Override
    public void completeResearch(Long demandId) {
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.RESEARCHING.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }
        demand.setStatus(DemandStatusEnum.COMPLETED.getCode());
        demandService.updateById(demand);
    }

    @Override
    public void closeDemand(Long demandId) {
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        if (Objects.equals(demand.getStatus(), DemandStatusEnum.COMPLETED.getCode())
                || Objects.equals(demand.getStatus(), DemandStatusEnum.CLOSED.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }
        demand.setStatus(DemandStatusEnum.CLOSED.getCode());
        demandService.updateById(demand);
    }

    @Override
    public void resubmitAudit(Long demandId) {
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.CLOSED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }
        demand.setStatus(DemandStatusEnum.REVIEWING.getCode());
        demandService.updateById(demand);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void kickMember(MemberChangeDTO memberChangeDTO) {
        DemandMemberChange demandMemberChange = new DemandMemberChange();
        BeanUtils.copyProperties(memberChangeDTO, demandMemberChange);

        demandMemberChange.setOperatorId(SecurityUtils.getCurrentUserId());
        demandMemberChange.setChangeType(MemberChangeTypeEnum.PUBLISHER_REMOVE.getCode());
        demandMemberChange.setStatus(MemberChangeStatusEnum.EFFECTIVE.getCode());
        demandMemberChange.setAuditTime(LocalDateTime.now());
        demandMemberChange.setAuditUserId(SecurityUtils.getCurrentUserId());

        demandMemberChangeMapper.insert(demandMemberChange);

        LambdaQueryWrapper<DemandMember> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper
                .eq(DemandMember::getUserId, memberChangeDTO.getUserId())
                .eq(DemandMember::getDemandId, memberChangeDTO.getDemandId());
        demandMemberMapper.delete(deleteWrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void auditMemberQuitApply(MemberChangeDTO memberChangeDTO) {
        LambdaQueryWrapper<DemandMemberChange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandMemberChange::getUserId, memberChangeDTO.getUserId())
                .eq(DemandMemberChange::getDemandId, memberChangeDTO.getDemandId());
        DemandMemberChange demandMemberChange = demandMemberChangeMapper.selectOne(queryWrapper);
        if (demandMemberChange == null) {
            throw new BusinessException(ResultCode.DEMAND_MEMBER_CHANGE_NOT_EXIST);
        }
        demandMemberChange.setStatus(memberChangeDTO.getStatus());
        demandMemberChange.setAuditUserId(SecurityUtils.getCurrentUserId());
        demandMemberChange.setAuditTime(LocalDateTime.now());
        demandMemberChangeMapper.updateById(demandMemberChange);

        if(Objects.equals(memberChangeDTO.getStatus(),MemberChangeStatusEnum.EFFECTIVE.getCode())){
            LambdaQueryWrapper<DemandMember> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper
                    .eq(DemandMember::getUserId, memberChangeDTO.getUserId())
                    .eq(DemandMember::getDemandId, memberChangeDTO.getDemandId());
            demandMemberMapper.delete(deleteWrapper);
        }
    }
}

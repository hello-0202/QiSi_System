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


/**
 * 实践审核服务实现类
 */
@RequiredArgsConstructor
@Service
public class PracticeAuditServiceImpl implements PracticeAuditService {


    private final DemandMemberChangeMapper demandMemberChangeMapper;
    private final DemandMemberMapper demandMemberMapper;
    private final DemandExecutionPlanMapper demandExecutionPlanMapper;
    private final DemandService demandService;


    /**
     * 提交需求执行方案
     */
    @Override
    public void submitDemandPlan(DemandPlanDTO demandPlanDTO) {
        // 1. 校验需求是否存在
        if(demandService.isNotExistsByDemandId(demandPlanDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 转换DTO为实体
        DemandExecutionPlan executionPlan = new DemandExecutionPlan();
        BeanUtils.copyProperties(demandPlanDTO, executionPlan);
        executionPlan.setOperatorId(SecurityUtils.getCurrentUserId());

        // 3. 插入执行方案
        demandExecutionPlanMapper.insert(executionPlan);
    }


    /**
     * 开始研究
     */
    @Override
    public void startResearch(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验需求状态
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.PUBLISHED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 3. 更新为研究中状态
        demand.setStatus(DemandStatusEnum.RESEARCHING.getCode());
        demandService.updateById(demand);
    }


    /**
     * 完成研究
     */
    @Override
    public void completeResearch(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验需求状态
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.RESEARCHING.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 3. 更新为已完成状态
        demand.setStatus(DemandStatusEnum.COMPLETED.getCode());
        demandService.updateById(demand);
    }


    /**
     * 关闭需求
     */
    @Override
    public void closeDemand(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验状态是否允许关闭
        if (Objects.equals(demand.getStatus(), DemandStatusEnum.COMPLETED.getCode())
                || Objects.equals(demand.getStatus(), DemandStatusEnum.CLOSED.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 3. 更新为已关闭状态
        demand.setStatus(DemandStatusEnum.CLOSED.getCode());
        demandService.updateById(demand);
    }


    /**
     * 重新提交审核
     */
    @Override
    public void resubmitAudit(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandService.getById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验是否为关闭状态
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.CLOSED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 3. 重新提交审核
        demand.setStatus(DemandStatusEnum.REVIEWING.getCode());
        demandService.updateById(demand);
    }


    /**
     * 踢出成员
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void kickMember(MemberChangeDTO memberChangeDTO) {
        // 1. 构建成员变更记录
        DemandMemberChange demandMemberChange = new DemandMemberChange();
        BeanUtils.copyProperties(memberChangeDTO, demandMemberChange);

        // 2. 设置变更信息
        demandMemberChange.setOperatorId(SecurityUtils.getCurrentUserId());
        demandMemberChange.setChangeType(MemberChangeTypeEnum.PUBLISHER_REMOVE.getCode());
        demandMemberChange.setStatus(MemberChangeStatusEnum.EFFECTIVE.getCode());
        demandMemberChange.setAuditTime(LocalDateTime.now());
        demandMemberChange.setAuditUserId(SecurityUtils.getCurrentUserId());

        // 3. 插入变更记录
        demandMemberChangeMapper.insert(demandMemberChange);

        // 4. 删除成员
        LambdaQueryWrapper<DemandMember> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper
                .eq(DemandMember::getUserId, memberChangeDTO.getUserId())
                .eq(DemandMember::getDemandId, memberChangeDTO.getDemandId());
        demandMemberMapper.delete(deleteWrapper);
    }


    /**
     * 审核成员退出申请
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void auditMemberQuitApply(MemberChangeDTO memberChangeDTO) {
        // 1. 查询成员变更记录
        LambdaQueryWrapper<DemandMemberChange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandMemberChange::getUserId, memberChangeDTO.getUserId())
                .eq(DemandMemberChange::getDemandId, memberChangeDTO.getDemandId());
        DemandMemberChange demandMemberChange = demandMemberChangeMapper.selectOne(queryWrapper);
        if (demandMemberChange == null) {
            throw new BusinessException(ResultCode.DEMAND_MEMBER_CHANGE_NOT_EXIST);
        }

        // 2. 更新审核信息
        demandMemberChange.setStatus(memberChangeDTO.getStatus());
        demandMemberChange.setAuditUserId(SecurityUtils.getCurrentUserId());
        demandMemberChange.setAuditTime(LocalDateTime.now());
        demandMemberChangeMapper.updateById(demandMemberChange);

        // 3. 审核通过则删除成员
        if(Objects.equals(memberChangeDTO.getStatus(),MemberChangeStatusEnum.EFFECTIVE.getCode())){
            LambdaQueryWrapper<DemandMember> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper
                    .eq(DemandMember::getUserId, memberChangeDTO.getUserId())
                    .eq(DemandMember::getDemandId, memberChangeDTO.getDemandId());
            demandMemberMapper.delete(deleteWrapper);
        }
    }
}
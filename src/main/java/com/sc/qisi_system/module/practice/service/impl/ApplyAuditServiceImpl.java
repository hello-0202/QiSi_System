package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.MemberStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.dto.AuditApplyDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.service.DemandApplyService;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.ApplyAuditService;
import com.sc.qisi_system.module.apply.mapper.DemandMemberMapper;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ApplyAuditServiceImpl implements ApplyAuditService {


    private final DemandMemberMapper demandMemberMapper;
    private final DemandService demandService;
    private final DemandApplyService demandApplyService;


    @Override
    public void approveApplyMember(Long userId,AuditApplyDTO auditApplyDTO) {
        // 1. 查询申请是否存在,检查权限
        checkApplyAndPermission(userId, auditApplyDTO);

        deleteDemandMember(auditApplyDTO.getDemandId(), auditApplyDTO.getApplicantId());
        addPassedDemandMember(auditApplyDTO.getDemandId(), auditApplyDTO.getApplicantId(), auditApplyDTO.getRoleType());
    }


    @Override
    public void rejectApplyMember(Long userId, AuditApplyDTO auditApplyDTO) {
        // 1. 查询申请是否存在,检查权限
        checkApplyAndPermission(userId, auditApplyDTO);
    }


    private void checkApplyAndPermission(Long userId, AuditApplyDTO auditApplyDTO) {
        DemandApply demandApply = demandApplyService.getById(auditApplyDTO.getDemandId());
        if (demandApply == null) {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        Demand demand = demandService.getById(demandApply.getDemandId());
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if (!demand.getPublisherId().equals(userId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }

        DemandApply apply = new DemandApply();
        apply.setId(auditApplyDTO.getApplyId());
        apply.setAuditUserId(userId);
        apply.setAuditStatus(auditApplyDTO.getAuditStatus());
        apply.setAuditRemark(auditApplyDTO.getAuditRemark());
        demandApplyService.updateById(apply);

        deleteDemandMember(auditApplyDTO.getDemandId(), auditApplyDTO.getApplicantId());
    }


    /**
     * 根据需求ID+用户ID 删除需求成员
     */
    private void deleteDemandMember(Long demandId, Long userId) {
        LambdaQueryWrapper<DemandMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandMember::getDemandId, demandId);
        wrapper.eq(DemandMember::getUserId, userId);
        demandMemberMapper.delete(wrapper);
    }


    /**
     * 新增通过的需求成员
     */
    private void addPassedDemandMember(Long demandId, Long userId, Integer roleType) {
        DemandMember demandMember = new DemandMember();
        demandMember.setUserId(userId);
        demandMember.setDemandId(demandId);
        demandMember.setStatus(MemberStatusEnum.PASSED.getCode());
        demandMember.setRoleType(roleType);
        demandMember.setJoinTime(LocalDateTime.now());
        demandMemberMapper.insert(demandMember);
    }
}

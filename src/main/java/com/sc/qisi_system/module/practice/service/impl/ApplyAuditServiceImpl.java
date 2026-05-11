package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.MemberStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.practice.dto.AuditApplyDTO;
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


/**
 * 申请审核服务实现类
 */
@RequiredArgsConstructor
@Service
public class ApplyAuditServiceImpl implements ApplyAuditService {


    private final DemandMemberMapper demandMemberMapper;
    private final DemandService demandService;
    private final DemandApplyService demandApplyService;


    /**
     * 审核通过需求申请成员
     */
    @Override
    public void approveApplyMember(Long userId,AuditApplyDTO auditApplyDTO) {
        // 1. 校验申请信息与操作权限
        checkApplyAndPermission(userId, auditApplyDTO);

        // 2. 删除旧的成员记录
        deleteDemandMember(auditApplyDTO.getDemandId(), auditApplyDTO.getApplicantId());

        // 3. 添加审核通过的成员
        addPassedDemandMember(auditApplyDTO.getDemandId(), auditApplyDTO.getApplicantId(), auditApplyDTO.getRoleType());
    }


    /**
     * 审核拒绝需求申请成员
     */
    @Override
    public void rejectApplyMember(Long userId, AuditApplyDTO auditApplyDTO) {
        // 1. 校验申请信息与操作权限
        checkApplyAndPermission(userId, auditApplyDTO);
    }


    /**
     * 校验申请信息与操作权限
     */
    private void checkApplyAndPermission(Long userId, AuditApplyDTO auditApplyDTO) {
        // 1. 查询申请记录是否存在
        DemandApply demandApply = demandApplyService.getById(auditApplyDTO.getApplyId());
        if (demandApply == null) {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        // 2. 查询需求信息并校验
        Demand demand = demandService.getById(demandApply.getDemandId());
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 3. 校验是否为需求发布者
        if (!demand.getPublisherId().equals(userId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }

        // 4. 更新申请审核状态
        DemandApply apply = new DemandApply();
        apply.setId(auditApplyDTO.getApplyId());
        apply.setAuditUserId(userId);
        apply.setAuditStatus(auditApplyDTO.getAuditStatus());
        apply.setAuditRemark(auditApplyDTO.getAuditRemark());
        apply.setAuditTime(LocalDateTime.now());
        demandApplyService.updateById(apply);

        // 5. 删除旧成员记录
        deleteDemandMember(auditApplyDTO.getDemandId(), auditApplyDTO.getApplicantId());
    }


    /**
     * 根据需求ID+用户ID 删除需求成员
     */
    private void deleteDemandMember(Long demandId, Long userId) {
        // 1. 构建删除条件
        LambdaQueryWrapper<DemandMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandMember::getDemandId, demandId);
        wrapper.eq(DemandMember::getUserId, userId);

        // 2. 执行删除
        demandMemberMapper.delete(wrapper);
    }


    /**
     * 新增审核通过的需求成员
     */
    private void addPassedDemandMember(Long demandId, Long userId, Integer roleType) {
        // 1. 构建成员实体
        DemandMember demandMember = new DemandMember();
        demandMember.setUserId(userId);
        demandMember.setDemandId(demandId);
        demandMember.setStatus(MemberStatusEnum.PASSED.getCode());
        demandMember.setRoleType(roleType);
        demandMember.setJoinTime(LocalDateTime.now());

        // 2. 插入成员记录
        demandMemberMapper.insert(demandMember);
    }
}
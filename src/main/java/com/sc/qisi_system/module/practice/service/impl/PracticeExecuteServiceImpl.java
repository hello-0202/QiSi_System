package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.enums.MemberChangeStatusEnum;
import com.sc.qisi_system.common.enums.MemberChangeTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.DemandProgressCalculator;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import com.sc.qisi_system.module.practice.entity.DemandExecutionPlan;
import com.sc.qisi_system.module.practice.entity.DemandMemberChange;
import com.sc.qisi_system.module.practice.entity.DemandProgress;
import com.sc.qisi_system.module.practice.mapper.DemandExecutionPlanMapper;
import com.sc.qisi_system.module.practice.mapper.DemandMemberChangeMapper;
import com.sc.qisi_system.module.practice.mapper.DemandProgressMapper;
import com.sc.qisi_system.module.practice.service.PracticeExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PracticeExecuteServiceImpl implements PracticeExecuteService {


    private final DemandProgressMapper demandProgressMapper;
    private final DemandExecutionPlanMapper demandExecutionPlanMapper;
    private final DemandMemberChangeMapper demandMemberChangeMapper;
    private final DemandService demandService;
    private final DemandProgressCalculator demandProgressCalculator;


    @Override
    public void submitDemandLog(DemandProgressDTO demandProgressDTO) {
        if (demandService.notExistsByDemandId(demandProgressDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        DemandProgress demandProgress = new DemandProgress();
        BeanUtils.copyProperties(demandProgressDTO, demandProgress);

        demandProgressMapper.insert(demandProgress);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDemandPlan(DemandPlanDTO demandPlanDTO) {
        DemandExecutionPlan demandExecutionPlan = demandExecutionPlanMapper.selectOne(Wrappers.lambdaQuery(DemandExecutionPlan.class)
                .eq(DemandExecutionPlan::getDemandId, demandPlanDTO.getDemandId()));
        if (demandExecutionPlan == null) {
            throw new BusinessException(ResultCode.DEMAND_PLAN_NOT_EXIST);
        }
        demandExecutionPlan.setResearchPlan(demandPlanDTO.getResearchPlan());
        demandExecutionPlan.setModifyRemark(demandPlanDTO.getModifyRemark());
        demandExecutionPlanMapper.updateById(demandExecutionPlan);

        Demand demand = demandService.getById(demandPlanDTO.getDemandId());
        demand.setProgressPercent(demandProgressCalculator.calculateProgress(demandExecutionPlan.getResearchPlan()));
        demandService.updateById(demand);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyQuitDemand(MemberChangeDTO memberChangeDTO) {
        if (demandService.notExistsByDemandId(memberChangeDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        long count = demandMemberChangeMapper.selectCount(Wrappers.lambdaQuery(DemandMemberChange.class)
                .eq(DemandMemberChange::getDemandId, memberChangeDTO.getDemandId())
                .eq(DemandMemberChange::getUserId, SecurityUtils.getCurrentUserId())
                .eq(DemandMemberChange::getStatus, MemberChangeStatusEnum.PENDING.getCode()));
        if (count > 0) {
            throw new BusinessException(ResultCode.DEMAND_QUIT_APPLIED_REPEAT);
        }

        DemandMemberChange demandMemberChanged = new DemandMemberChange();
        BeanUtils.copyProperties(memberChangeDTO, demandMemberChanged);
        demandMemberChanged.setChangeType(MemberChangeTypeEnum.QUIT_APPLY.getCode());
        demandMemberChanged.setStatus(MemberChangeStatusEnum.PENDING.getCode());
        demandMemberChanged.setOperatorId(SecurityUtils.getCurrentUserId());

        demandMemberChangeMapper.insert(demandMemberChanged);
    }
}

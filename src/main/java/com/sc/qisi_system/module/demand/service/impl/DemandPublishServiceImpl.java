package com.sc.qisi_system.module.demand.service.impl;

import com.sc.qisi_system.common.enums.CloseApplyStatusEnum;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandPublishService;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DemandPublishServiceImpl implements DemandPublishService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;


    @Override
    public Long submitDraft(Long userId,DemandPublishDraftDTO demandPublishDraftDTO) {

        if(sysUserService.existsById(userId)){
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 1. 转换对象
        Demand demand = new Demand();
        BeanUtils.copyProperties(demandPublishDraftDTO, demand);

        // 2. 设置相关状态
        demand.setPublisherId(userId);
        demand.setStatus(DemandStatusEnum.DRAFT.getCode());
        demand.setCloseApplyStatus(CloseApplyStatusEnum.NO_APPLY.getCode());
        demand.setProgressPercent(0);

        demandMapper.insert(demand);

        return demand.getId();
    }


    @Override
    public Long updateDraft(DemandUpdateDraftDTO demandUpdateDraftDTO) {

        // 1. 更新前校验
        if (demandUpdateDraftDTO.getId() == null) {
            throw new BusinessException(ResultCode.DEMAND_ID_NULL);
        }
        Demand demand = demandMapper.selectById(demandUpdateDraftDTO.getId());
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.ONLY_ALLOW_DRAFT_EDIT);
        }

        // 2. 更新需求
        Demand updatedDemand = Demand.builder()
                .id(demandUpdateDraftDTO.getId())
                .publishType(demandUpdateDraftDTO.getPublishType())
                .title(demandUpdateDraftDTO.getTitle())
                .category(demandUpdateDraftDTO.getCategory())
                .researchField(demandUpdateDraftDTO.getResearchField())
                .background(demandUpdateDraftDTO.getBackground())
                .description(demandUpdateDraftDTO.getDescription())
                .techRequire(demandUpdateDraftDTO.getTechRequire())
                .startTime(demandUpdateDraftDTO.getStartTime())
                .endTime(demandUpdateDraftDTO.getEndTime())
                .researchCycle(demandUpdateDraftDTO.getResearchCycle())
                .maxMembers(demandUpdateDraftDTO.getMaxMembers())
                .requirePlan(demandUpdateDraftDTO.getRequirePlan())
                .progressPercent(0)
                .build();

        demandMapper.updateById(updatedDemand);

        return demand.getId();
    }


    @Override
    public Long submitAudit(Long demandId) {

        // 1. 提交审核前校验
        Demand demand = demandMapper.selectById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 2. 提交审核
        demand.setStatus(DemandStatusEnum.REVIEWING.getCode());
        demandMapper.updateById(demand);

        return demand.getId();
    }


    @Override
    public Long cancelSubmit(Long userId,Long demandId) {

        Demand demand = demandMapper.selectById(demandId);

        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        if(!Objects.equals(demand.getPublisherId(), userId)){
            throw new BusinessException(ResultCode.OPERATE_NOT_ALLOWED);
        }

        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.REVIEWING.getCode())) {
            throw new BusinessException(ResultCode.ONLY_REVIEWING_CANCEL);
        }

        demand.setStatus(DemandStatusEnum.DRAFT.getCode());
        demandMapper.updateById(demand);

        return demand.getId();
    }


}

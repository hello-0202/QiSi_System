package com.sc.qisi_system.module.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.AuditStatusEnum;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.mapper.DemandApplyMapper;
import com.sc.qisi_system.module.apply.service.DemandApplyService;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal.DecimalMaxValidatorForNumber;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@RequiredArgsConstructor
@Service
public class DemandApplyServiceImpl implements DemandApplyService {


    private final DemandApplyMapper demandApplyMapper;
    private final DemandService demandService;
    private final DecimalMaxValidatorForNumber decimalMaxValidatorForNumber;


    @Override
    public void submitApply(Long userId, DemandApplyDTO demandApplyDTO) {

        Demand demand = demandService.getById(demandApplyDTO.getDemandId());

        if(demand == null)  {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if(!Objects.equals(demand.getStatus(),DemandStatusEnum.PUBLISHED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandApply::getDemandId, demandApplyDTO.getDemandId())
                .eq(DemandApply::getUserId, userId);

        if(!demandApplyMapper.exists(queryWrapper)){
            throw new BusinessException(ResultCode.DEMAND_APPLIED_REPEAT);
        }

        DemandApply demandApply = new DemandApply();
        demandApply.setUserId(userId);
        BeanUtils.copyProperties(demandApplyDTO, demandApply);

        demandApply.setAuditStatus(AuditStatusEnum.PENDING.getCode());

        demandApplyMapper.insert(demandApply);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelApply(Long userId, Long demandId) {

        Demand demand = demandService.getById(demandId);
        if(demand == null)  {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandApply::getDemandId, demandId)
                .eq(DemandApply::getUserId, userId);
        DemandApply demandApply = demandApplyMapper.selectOne(queryWrapper);

        if(demandApply == null)  {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }
        if(!Objects.equals(demandApply.getAuditStatus(),AuditStatusEnum.PENDING.getCode())){
            throw new BusinessException(ResultCode.DEMAND_APPLY_STATUS_NOT_ALLOW);
        }

        demandApplyMapper.deleteById(demandApply.getId());

    }
}

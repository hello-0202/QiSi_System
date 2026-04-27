package com.sc.qisi_system.module.practice.service.impl;

import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.entity.DemandProgress;
import com.sc.qisi_system.module.practice.mapper.DemandProgressMapper;
import com.sc.qisi_system.module.practice.service.PracticeExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PracticeExecuteServiceImpl implements PracticeExecuteService {


    private final DemandProgressMapper demandProgressMapper;
    private final DemandService demandService;


    @Override
    public void submitDemandPlan(DemandProgressDTO demandProgressDTO) {
        if(demandService.notExistsByDemandId(demandProgressDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        DemandProgress demandProgress = new DemandProgress();
        BeanUtils.copyProperties(demandProgressDTO, demandProgress);

        demandProgressMapper.insert(demandProgress);
    }
}

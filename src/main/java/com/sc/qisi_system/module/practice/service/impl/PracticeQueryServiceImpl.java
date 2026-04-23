package com.sc.qisi_system.module.practice.service.impl;

import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import com.sc.qisi_system.module.apply.vo.ApplyMemberListVO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandReceiverDetailVO;
import com.sc.qisi_system.module.demand.vo.MyDemandDetailVO;
import com.sc.qisi_system.module.practice.service.PracticeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PracticeQueryServiceImpl implements PracticeQueryService {


    private final DemandQueryService demandQueryService;
    private final ApplyQueryService applyQueryService;


    @Override
    public PageResult<DemandListVO> getPracticeDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO){

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
    public PageResult<DemandListVO> getMyPracticeList(Long userId,PracticeDemandQueryDTO practiceDemandQueryDTO) {
        return demandQueryService.getMyPracticeDemandList(userId,practiceDemandQueryDTO);
    }


    @Override
    public DemandReceiverDetailVO getMyPracticeDetail(Long demandId) {
        return demandQueryService.getDemandReceiverDetail(demandId);
    }


    @Override
    public List<ApplyMemberListVO> getMemberList(Long userId, Long demandId) {
        return applyQueryService.getApplyMemberList(userId,demandId);
    }


    @Override
    public ApplyMemberListVO getMemberDetailInfo(Long userId) {
        return applyQueryService.getApplyMemberDetail(userId);
    }
}

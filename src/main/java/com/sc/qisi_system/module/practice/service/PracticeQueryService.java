package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.vo.ApplyMemberListVO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandReceiverDetailVO;
import com.sc.qisi_system.module.demand.vo.MyDemandDetailVO;

import java.util.List;

public interface PracticeQueryService {


    PageResult<DemandListVO> getPracticeDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    MyDemandDetailVO getPracticeDemandDetail(Long demandId);


    PageResult<DemandListVO> getMyPracticeList(Long userId,PracticeDemandQueryDTO practiceDemandQueryDTO);


    DemandReceiverDetailVO getMyPracticeDetail(Long demandId);


    List<ApplyMemberListVO> getMemberList(Long userId, Long demandId);


    ApplyMemberListVO getMemberDetailInfo(Long userId);



}

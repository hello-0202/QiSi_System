package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;

public interface PracticeAuditService {


    void submitDemandPlan(DemandPlanDTO demandPlanDTO);


    void startResearch(Long demandId);


    void completeResearch(Long demandId);


    void closeDemand(Long demandId);


    void resubmitAudit(Long demandId);


    void kickMember(MemberChangeDTO memberChangeDTO);


    void auditMemberQuitApply(MemberChangeDTO memberChangeDTO);
}

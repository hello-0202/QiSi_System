package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;

public interface PracticeAuditService {


    /**
     * 提交需求计划
     * 角色: 发布者
     *
     * @param demandPlanDTO 需求计划请求体
     */
    void submitDemandPlan(DemandPlanDTO demandPlanDTO);


    /**
     * 开始研究
     * 角色: 发布者
     *
     * @param demandId 需求id
     */
    void startResearch(Long demandId);


    /**
     * 完成研究
     * 角色: 发布者
     *
     * @param demandId 需求id
     */
    void completeResearch(Long demandId);


    /**
     * 关闭需求
     * 角色: 发布者
     *
     * @param demandId 需求id
     */
    void closeDemand(Long demandId);


    /**
     * 重新提交审核
     * 角色: 发布者
     *
     * @param demandId 需求id
     */
    void resubmitAudit(Long demandId);


    /**
     * 踢出成员
     * 角色: 发布者
     *
     * @param memberChangeDTO 需求成员更改请求体
     */
    void kickMember(MemberChangeDTO memberChangeDTO);


    /**
     * 发布者审核成员退出申请
     *
     * @param memberChangeDTO 需求成员变更请求体
     */
    void auditMemberQuitApply(MemberChangeDTO memberChangeDTO);
}

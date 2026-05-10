package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.module.practice.dto.AuditApplyDTO;


/**
 * 申请审核业务服务接口
 * 提供对成员加入申请的审核（通过、拒绝）功能
 */
public interface ApplyAuditService {


    /**
     * 审核通过申请成员
     * 角色: 发布者
     *
     * @param userId 审核人用户ID
     * @param auditApplyDTO 审核请求参数
     */
    void approveApplyMember(Long userId, AuditApplyDTO auditApplyDTO);


    /**
     * 审核拒绝申请成员
     * 角色: 发布者
     *
     * @param userId 审核人用户ID
     * @param auditApplyDTO 审核请求参数
     */
    void rejectApplyMember(Long userId, AuditApplyDTO auditApplyDTO);
}
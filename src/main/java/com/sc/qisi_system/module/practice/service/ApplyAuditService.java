package com.sc.qisi_system.module.practice.service;


import com.sc.qisi_system.module.apply.dto.AuditApplyDTO;

public interface ApplyAuditService {


    /**
     * 审核通过申请成员接口
     *
     * @param auditApplyDTO 请求体
     */
    void approveApplyMember(Long userId, AuditApplyDTO auditApplyDTO);


    /**
     * 审核拒绝申请成员
     *
     * @param auditApplyDTO 请求体
     */
    void rejectApplyMember(Long userId, AuditApplyDTO auditApplyDTO);
}

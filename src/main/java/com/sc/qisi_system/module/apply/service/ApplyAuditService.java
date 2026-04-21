package com.sc.qisi_system.module.apply.service;


import com.sc.qisi_system.module.apply.dto.AuditApplyDTO;

public interface ApplyAuditService {


    void approveApplyMember(Long userId,AuditApplyDTO auditApplyDTO);


    void rejectApplyMember(Long userId,AuditApplyDTO auditApplyDTO);
}

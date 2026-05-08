package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.AdminDemandQueryDTO;
import com.sc.qisi_system.module.admin.dto.AuditDemandDTO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;

public interface AdminDemandService {


    PageResult<DemandListVO> getDemandAuditList(Integer pageNum, Integer pageSize);


    DemandPublicDetailVO getDemandDetail(Long demandId);


    void auditDemand(AuditDemandDTO auditDemandDTO);


    PageResult<DemandListVO> getDemandList(AdminDemandQueryDTO adminDemandQueryDTO);
}

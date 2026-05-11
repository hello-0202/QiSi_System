package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.AdminDemandQueryDTO;
import com.sc.qisi_system.module.admin.dto.AuditDemandDTO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;


/**
 * 管理员需求管理服务接口
 * 功能: 需求审核列表查询、需求详情查看、需求审核、需求条件查询等管理员业务逻辑
 */
public interface AdminDemandService {


    /**
     * 查询需求审核列表
     * 角色: 管理员
     *
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 需求审核分页列表
     */
    PageResult<DemandListVO> getDemandAuditList(Integer pageNum, Integer pageSize);


    /**
     * 查询需求详情
     * 角色: 管理员
     *
     * @param demandId 需求ID
     * @return 需求详细信息
     */
    DemandPublicDetailVO getDemandDetail(Long demandId);


    /**
     * 审核需求
     * 角色: 管理员
     *
     * @param auditDemandDTO 审核请求参数
     */
    void auditDemand(AuditDemandDTO auditDemandDTO);


    /**
     * 条件查询所有需求
     * 角色: 管理员
     *
     * @param adminDemandQueryDTO 需求查询条件
     * @return 需求分页列表
     */
    PageResult<DemandListVO> getDemandList(AdminDemandQueryDTO adminDemandQueryDTO);
}
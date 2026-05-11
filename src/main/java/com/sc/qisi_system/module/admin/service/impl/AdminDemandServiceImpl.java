package com.sc.qisi_system.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.admin.dto.AdminDemandQueryDTO;
import com.sc.qisi_system.module.admin.dto.AuditDemandDTO;
import com.sc.qisi_system.module.admin.service.AdminDemandService;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * 管理员需求审核服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminDemandServiceImpl implements AdminDemandService {


    private final DemandService demandService;


    /**
     * 获取待审核需求列表
     */
    @Override
    public PageResult<DemandListVO> getDemandAuditList(Integer pageNum, Integer pageSize) {
        // 1. 构建分页对象
        Page<Demand> page = new Page<>(pageNum, pageSize);

        // 2. 构造查询条件：只查询待审核状态，按创建时间倒序
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Demand::getStatus, DemandStatusEnum.REVIEWING.getCode())
                .orderByDesc(Demand::getCreateTime);

        // 3. 分页查询需求数据
        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        // 4. 转换并返回管理员视图分页结果
        return demandService.convertToAdminPageResultList(demandIPage);
    }


    /**
     * 获取需求详情
     */
    @Override
    public DemandPublicDetailVO getDemandDetail(Long demandId) {
        // 1. 判断需求是否存在，不存在则抛出异常
        if(!demandService.isNotExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        // 2. 查询并返回公开需求详情
        return demandService.getPublicDemandDetail(demandId);
    }


    /**
     * 审核需求
     */
    @Override
    public void auditDemand(AuditDemandDTO auditDemandDTO) {
        // 1. 校验需求是否存在
        if(demandService.isNotExistsByDemandId(auditDemandDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 获取需求信息并设置审核字段
        Demand demand = demandService.getById(auditDemandDTO.getDemandId());
        demand.setStatus(auditDemandDTO.getStatus());
        demand.setAuditUserId(SecurityUtils.getCurrentUserId());
        demand.setAuditRemark(auditDemandDTO.getAuditRemark());
        demand.setAuditTime(LocalDateTime.now());

        // 3. 更新需求信息
        demandService.updateById(demand);
    }


    /**
     * 条件查询所有需求列表
     */
    @Override
    public PageResult<DemandListVO> getDemandList(AdminDemandQueryDTO adminDemandQueryDTO) {
        // 1. 构建分页对象
        Page<Demand> page = new Page<>(adminDemandQueryDTO.getPageNum(), adminDemandQueryDTO.getPageSize());

        // 2. 构造动态查询条件，按创建时间倒序
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(adminDemandQueryDTO.getStatus() != null, Demand::getStatus, adminDemandQueryDTO.getStatus())
                .eq(adminDemandQueryDTO.getCategory() != null, Demand::getCategory, adminDemandQueryDTO.getCategory())
                .orderByDesc(Demand::getCreateTime);

        // 3. 分页查询需求数据
        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        // 4. 转换并返回管理员视图分页结果
        return demandService.convertToAdminPageResultList(demandIPage);
    }
}
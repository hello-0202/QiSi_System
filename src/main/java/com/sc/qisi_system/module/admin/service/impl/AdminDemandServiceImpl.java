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


@RequiredArgsConstructor
@Service
public class AdminDemandServiceImpl implements AdminDemandService {


    private final DemandService demandService;


    @Override
    public PageResult<DemandListVO> getDemandAuditList(Integer pageNum, Integer pageSize) {
        Page<Demand> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Demand::getStatus, DemandStatusEnum.REVIEWING.getCode())
                .orderByDesc(Demand::getCreateTime);

        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        return demandService.convertToAdminPageResultList(demandIPage);

    }


    @Override
    public DemandPublicDetailVO getDemandDetail(Long demandId) {
        if(!demandService.isNotExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        return demandService.getPublicDemandDetail(demandId);
    }


    @Override
    public void auditDemand(AuditDemandDTO auditDemandDTO) {
        if(demandService.isNotExistsByDemandId(auditDemandDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        Demand demand = demandService.getById(auditDemandDTO.getDemandId());
        demand.setStatus(auditDemandDTO.getStatus());
        demand.setAuditUserId(SecurityUtils.getCurrentUserId());
        demand.setAuditRemark(auditDemandDTO.getAuditRemark());
        demand.setAuditTime(LocalDateTime.now());
        demandService.updateById(demand);
    }

    @Override
    public PageResult<DemandListVO> getDemandList(AdminDemandQueryDTO adminDemandQueryDTO) {
        Page<Demand> page = new Page<>(adminDemandQueryDTO.getPageNum(), adminDemandQueryDTO.getPageSize());
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(adminDemandQueryDTO.getStatus() != null, Demand::getStatus, adminDemandQueryDTO.getStatus())
                .eq(adminDemandQueryDTO.getCategory() != null, Demand::getCategory, adminDemandQueryDTO.getCategory())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        return demandService.convertToAdminPageResultList(demandIPage);
    }
}

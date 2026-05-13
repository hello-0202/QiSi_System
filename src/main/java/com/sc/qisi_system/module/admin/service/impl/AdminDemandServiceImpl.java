package com.sc.qisi_system.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 管理员需求审核服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminDemandServiceImpl implements AdminDemandService {


    private final DemandService demandService;
    private final SysUserService sysUserService;


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
        //TODO
        // 2. 查询并返回公开需求详情
        return demandService.getPublicDemandDetail(demandId);
    }


    /**
     * 审核需求
     */
    @Override
    public void auditDemand(AuditDemandDTO auditDemandDTO) {
        // 1. 校验需求是否存在
        if(!demandService.isNotExistsByDemandId(auditDemandDTO.getDemandId())) {
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
     * 条件查询所有需求列表（管理员）
     */
    @Override
    public PageResult<DemandListVO> getDemandList(AdminDemandQueryDTO adminDemandQueryDTO) {
        // 1. 分页
        Page<Demand> page = new Page<>(adminDemandQueryDTO.getPageNum(), adminDemandQueryDTO.getPageSize());

        // 2. 条件构造
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        String name = adminDemandQueryDTO.getName();
        if (StringUtils.hasText(name)) {
            // 1. 根据姓名模糊查询 所有用户ID
            LambdaQueryWrapper<SysUser> userQuery = new LambdaQueryWrapper<>();
            userQuery.like(SysUser::getName, name).select(SysUser::getId);
            List<Long> userIds = sysUserService.list(userQuery).stream()
                    .map(SysUser::getId)
                    .collect(Collectors.toList());

            // 2. 把用户ID 作为 publisherId 查询需求
            queryWrapper.in(CollectionUtils.isNotEmpty(userIds), Demand::getPublisherId, userIds);
        }

        queryWrapper
                .eq(adminDemandQueryDTO.getCategory() != null && adminDemandQueryDTO.getCategory() != 0,
                        Demand::getCategory, adminDemandQueryDTO.getCategory())
                .eq(adminDemandQueryDTO.getStatus() != null && adminDemandQueryDTO.getStatus() != 0,
                        Demand::getStatus, adminDemandQueryDTO.getStatus())
                .ge(adminDemandQueryDTO.getCreateTime() != null, Demand::getCreateTime, adminDemandQueryDTO.getCreateTime())
                .orderByDesc(Demand::getCreateTime);

        // 3. 分页查询
        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        // 4. 转换VO
        return demandService.convertToAdminPageResultList(demandIPage);
    }
}
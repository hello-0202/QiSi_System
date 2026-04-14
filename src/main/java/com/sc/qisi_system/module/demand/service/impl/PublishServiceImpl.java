package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.CloseApplyStatusEnum;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.PublishService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandVO;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PublishServiceImpl implements PublishService {


    private final DemandMapper demandMapper;
    private final SysUserMapper sysUserMapper;


    @Override
    public Result submitDraft(DemandPublishDraftDTO demandPublishDraftDTO) {

        if(sysUserMapper.selectById(demandPublishDraftDTO.getPublisherId()) == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 1. 转换对象
        Demand demand = new Demand();
        BeanUtils.copyProperties(demandPublishDraftDTO, demand);

        // 2. 设置相关状态
        demand.setStatus(DemandStatusEnum.DRAFT.getCode());
        demand.setCloseApplyStatus(CloseApplyStatusEnum.NO_APPLY.getCode());

        demandMapper.insert(demand);

        return Result.success(demand.getId());
    }


    @Override
    public Result updateDraft(DemandUpdateDraftDTO demandUpdateDraftDTO) {

        // 1. 更新前校验
        if (demandUpdateDraftDTO.getId() == null) {
            throw new BusinessException(ResultCode.DEMAND_ID_NULL);
        }
        Demand demand = demandMapper.selectById(demandUpdateDraftDTO.getId());
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.ONLY_ALLOW_DRAFT_EDIT);
        }

        // 2. 更新需求
        BeanUtils.copyProperties(demandUpdateDraftDTO, demand);
        demandMapper.updateById(demand);

        return Result.success(demand.getId());
    }


    @Override
    public Result submitAudit(Long demandId) {

        // 1. 提交审核前校验
        Demand demand = demandMapper.selectById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 2. 提交审核
        demand.setStatus(DemandStatusEnum.REVIEWING.getCode());
        demandMapper.updateById(demand);

        return Result.success(demand.getId());
    }


    @Override
    public Result getDraftList(Long userId, Integer pageNum, Integer pageSize) {

        // 1. 设置查询参数
        Page<Demand> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Demand::getPublisherId, userId)
                .eq(Demand::getStatus, DemandStatusEnum.DRAFT.getCode())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        // 2. 转换为 DemandVO
        List<Demand> draftList = demandIPage.getRecords();
        List<DemandListVO> demandVOList = draftList.stream()
                .map(demand -> {
                    DemandListVO demandListVO = new DemandListVO();
                    BeanUtils.copyProperties(demand, demandListVO);
                    return demandListVO;
                }).toList();

        // 3. 包装返回
        PageResult<DemandListVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandIPage.getTotal());
        pageResult.setPages(demandIPage.getPages());
        pageResult.setRecords(demandVOList);

        return Result.success(pageResult);
    }

    @Override
    public Result getDemandDetail(Long demandId) {

        Demand demand = demandMapper.selectById(demandId);

        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        DemandVO demandVO = new DemandVO();
        BeanUtils.copyProperties(demand, demandVO);

        return Result.success(demandVO);
    }


}

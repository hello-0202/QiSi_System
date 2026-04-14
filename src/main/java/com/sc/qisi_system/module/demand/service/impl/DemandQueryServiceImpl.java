package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.demand.dto.DemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DemandQueryServiceImpl implements DemandQueryService {


    private final DemandMapper demandMapper;


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
        PageResult<DemandListVO> pageResult = convertToPageResult(demandIPage);

        return Result.success(pageResult);
    }

    @Override
    public Result getDemandList(Long userId, DemandQueryDTO demandQueryDTO) {

        Page<Demand> page = new Page<>(demandQueryDTO.getPageNum(), demandQueryDTO.getPageSize());
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Demand::getPublisherId, userId)
                .gt(Demand::getStatus, DemandStatusEnum.DRAFT.getCode());

        if (demandQueryDTO.getCreateTime() != null) {
            queryWrapper.ge(Demand::getCreateTime, demandQueryDTO.getCreateTime());
        }
        if (CollectionUtils.isNotEmpty(demandQueryDTO.getStatusList())) {
            queryWrapper.in(Demand::getStatus, demandQueryDTO.getStatusList());
        }

        queryWrapper.orderByDesc(Demand::getCreateTime)
                .orderByDesc(Demand::getProgressPercent);

        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);
        PageResult<DemandListVO> pageResult = convertToPageResult(demandIPage);

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


    /**
     * 公共方法：将 Demand 分页结果 转换为 DemandListVO 分页结果
     */
    private PageResult<DemandListVO> convertToPageResult(IPage<Demand> demandIPage) {
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {
                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);
                    return vo;
                }).toList();

        PageResult<DemandListVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandIPage.getTotal());
        pageResult.setPages(demandIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }
}

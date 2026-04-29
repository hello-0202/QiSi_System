package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.service.ApplyService;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.domain.DemandPublisherList;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.user.domain.UserInfoBase;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DemandServiceImpl extends ServiceImpl<DemandMapper, Demand> implements DemandService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final ApplyService applyService;


    @Override
    public boolean notExistsByDemandId(Long demandId) {
        return getById(demandId) != null;
    }


    /**
     *
     */
    @Override
    public PageResult<DemandListVO> convertToApplyPageResultList(IPage<Demand> demandIPage, Map<Long, DemandApplyList> applyStatusMap) {

        // 1. 转换VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {

                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);

                    // 2. 设置发布人信息
                    UserInfoBase userBase = sysUserService.getDemandUserBase(demand.getPublisherId());
                    DemandPublisherList userListVO = new DemandPublisherList();
                    BeanUtils.copyProperties(userBase, userListVO);
                    vo.setDemandPublisherList(userListVO);
                    DemandApplyList applyListVO = new DemandApplyList();

                    // 3. 设置申请信息
                    applyListVO.setId(applyStatusMap.get(demand.getId()).getId());
                    applyListVO.setAuditStatus(applyStatusMap.get(demand.getId()).getAuditStatus());
                    vo.setDemandApplyList(applyListVO);

                    return vo;
                }).toList();

        // 3. 返回包装
        PageResult<DemandListVO> result = new PageResult<>();
        result.setTotal(demandIPage.getTotal());
        result.setPages(demandIPage.getPages());
        result.setRecords(voList);
        return result;
    }


    @Override
    public PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO) {

        // 1. 设置分页查询
        Page<Demand> page = new Page<>(myApplyQueryDTO.getPageNum(), myApplyQueryDTO.getPageSize());

        // 2. 查询申请列表
        List<DemandApply> demandApplyList = applyService
                .list(new LambdaQueryWrapper<>(DemandApply.class)
                        .eq(DemandApply::getUserId,userId));
        if (demandApplyList.isEmpty()) {
            PageResult<DemandListVO> empty = new PageResult<>();
            empty.setTotal(0L);
            empty.setRecords(Collections.emptyList());
            empty.setPages(0);
            return empty;
        }

        // 3. 查询需求列表
        LambdaQueryWrapper<Demand> demandQuery = new LambdaQueryWrapper<>();
        demandQuery
                .in(Demand::getId,demandApplyList.stream().map(DemandApply::getDemandId).toList())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandMapper.selectPage(page, demandQuery);

        return convertToApplyPageResultList(demandIPage,applyService.getUserApplyMap(userId));
    }
}

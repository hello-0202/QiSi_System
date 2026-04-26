package com.sc.qisi_system.module.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.mapper.DemandApplyMapper;
import com.sc.qisi_system.module.apply.service.ApplyService;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ApplyServiceImpl extends ServiceImpl<DemandApplyMapper,DemandApply> implements ApplyService {

    private final DemandApplyMapper demandApplyMapper;

    @Override
    public Map<Long, DemandApplyList> getUserApplyMap(Long userId) {

        // 1. 查询用户所有申请
        List<DemandApply> applyList = demandApplyMapper.selectList(new LambdaQueryWrapper<DemandApply>()
                .eq(DemandApply::getUserId, userId));

        return applyList.stream()
                .collect(Collectors.toMap(
                        DemandApply::getDemandId,
                        apply -> {
                            DemandApplyList vo = new DemandApplyList();
                            vo.setId(apply.getId());
                            vo.setAuditStatus(apply.getAuditStatus());
                            return vo;
                        }
                ));
    }
}

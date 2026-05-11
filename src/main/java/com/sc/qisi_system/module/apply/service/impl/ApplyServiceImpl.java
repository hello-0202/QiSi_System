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


/**
 * 需求申请服务实现类
 */
@RequiredArgsConstructor
@Service
public class ApplyServiceImpl extends ServiceImpl<DemandApplyMapper,DemandApply> implements ApplyService {


    private final DemandApplyMapper demandApplyMapper;


    /**
     * 获取用户申请状态映射表
     */
    @Override
    public Map<Long, DemandApplyList> getUserApplyMap(Long userId) {
        // 1. 查询当前用户所有需求申请记录
        List<DemandApply> applyList = demandApplyMapper.selectList(new LambdaQueryWrapper<DemandApply>()
                .eq(DemandApply::getUserId, userId));

        // 2. 转换为需求ID -> 申请信息的Map结构并返回
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
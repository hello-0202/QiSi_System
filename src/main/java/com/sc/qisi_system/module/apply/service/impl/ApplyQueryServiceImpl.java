package com.sc.qisi_system.module.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.mapper.DemandApplyMapper;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.demand.vo.DemandApplyListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ApplyQueryServiceImpl implements ApplyQueryService {


    private final DemandApplyMapper demandApplyMapper;


    @Override
    public ApplyDetailVO getApplyDetail(Long applyId) {

        DemandApply demandApply = demandApplyMapper.selectById(applyId);

        if(demandApply == null) {
            throw new BusinessException( ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        ApplyDetailVO applyDetailVO = new ApplyDetailVO();
        BeanUtils.copyProperties(demandApply, applyDetailVO);

        return applyDetailVO;
    }





    /**
     * 获取当前用户的 需求ID->申请信息 映射
     */
    @Override
    public Map<Long, DemandApplyListVO> getUserApplyMap(Long userId) {

        // 1. 查询用户所有申请
        List<DemandApply> applyList = demandApplyMapper.selectList(new LambdaQueryWrapper<DemandApply>()
                .eq(DemandApply::getUserId, userId));

        return applyList.stream()
                .collect(Collectors.toMap(
                        DemandApply::getDemandId,
                        apply -> {
                            DemandApplyListVO vo = new DemandApplyListVO();
                            vo.setId(apply.getId());           // 申请ID
                            vo.setAuditStatus(apply.getAuditStatus()); // 状态
                            return vo;
                        }
                ));
    }


}

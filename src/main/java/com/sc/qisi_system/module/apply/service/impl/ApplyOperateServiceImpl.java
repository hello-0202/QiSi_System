package com.sc.qisi_system.module.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.AuditStatusEnum;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.dto.ApplyUpdateDTO;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.mapper.DemandApplyMapper;
import com.sc.qisi_system.module.apply.service.ApplyOperateService;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
 * 需求申请操作服务实现类
 */
@RequiredArgsConstructor
@Service
public class ApplyOperateServiceImpl implements ApplyOperateService {


    private final DemandApplyMapper demandApplyMapper;
    private final DemandService demandService;


    /**
     * 提交需求认领申请
     */
    @Override
    public void submitApply(Long userId, DemandApplyDTO demandApplyDTO) {
        // 1. 根据ID查询需求信息
        Demand demand = demandService.getById(demandApplyDTO.getDemandId());

        // 2. 校验需求是否存在
        if(demand == null)  {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 3. 校验需求状态是否为已发布
        if(!Objects.equals(demand.getStatus(), DemandStatusEnum.PUBLISHED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 4. 构造查询条件，判断是否重复申请
        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandApply::getDemandId, demandApplyDTO.getDemandId())
                .eq(DemandApply::getUserId, userId);

        // 5. 判断是否已存在申请记录
        if(demandApplyMapper.exists(queryWrapper)){
            throw new BusinessException(ResultCode.DEMAND_APPLIED_REPEAT);
        }

        // 6. 构建申请实体并赋值
        DemandApply demandApply = new DemandApply();
        demandApply.setUserId(userId);
        BeanUtils.copyProperties(demandApplyDTO, demandApply);

        // 7. 设置审核状态为待审核
        demandApply.setAuditStatus(AuditStatusEnum.PENDING.getCode());

        // 8. 插入申请记录
        demandApplyMapper.insert(demandApply);
    }


    /**
     * 修改需求认领申请信息
     */
    @Override
    public void updateApply(Long userId, ApplyUpdateDTO applyUpdateDTO) {
        // 1. 根据ID查询原申请记录
        DemandApply oldDemandApply = demandApplyMapper.selectById(applyUpdateDTO.getApplyId());

        // 2. 校验申请记录是否存在
        if(oldDemandApply == null)  {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        // 3. 校验操作权限
        if (!Objects.equals(oldDemandApply.getUserId(), userId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }

        // 4. 校验申请状态是否允许修改
        if(!Objects.equals(oldDemandApply.getAuditStatus(),AuditStatusEnum.PENDING.getCode())){
            throw new BusinessException(ResultCode.DEMAND_APPLY_STATUS_NOT_ALLOW);
        }

        // 5. 查询需求信息并校验
        Demand demand = demandService.getById(applyUpdateDTO.getDemandId());
        if(demand == null)  {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if(!Objects.equals(demand.getStatus(), DemandStatusEnum.PUBLISHED.getCode())){
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 6. 构建更新对象并赋值
        DemandApply demandApply = new DemandApply();
        demandApply.setId(applyUpdateDTO.getApplyId());
        demandApply.setResearchIdea(applyUpdateDTO.getResearchIdea());
        demandApply.setResearchPlan(applyUpdateDTO.getResearchPlan());
        demandApply.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        demandApply.setExpectedFinishTime(applyUpdateDTO.getExpectedFinishTime());
        demandApply.setRelevantExperience(applyUpdateDTO.getRelevantExperience());

        // 7. 更新申请信息
        demandApplyMapper.updateById(demandApply);
    }


    /**
     * 取消需求认领申请
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelApply(Long userId, Long demandId) {
        // 1. 查询需求信息并校验是否存在
        Demand demand = demandService.getById(demandId);
        if(demand == null)  {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 构造查询条件，查询申请记录
        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DemandApply::getDemandId, demandId)
                .eq(DemandApply::getUserId, userId);
        DemandApply demandApply = demandApplyMapper.selectOne(queryWrapper);

        // 3. 校验申请记录是否存在
        if(demandApply == null)  {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        // 4. 校验申请状态是否允许取消
        if(!Objects.equals(demandApply.getAuditStatus(),AuditStatusEnum.PENDING.getCode())){
            throw new BusinessException(ResultCode.DEMAND_APPLY_STATUS_NOT_ALLOW);
        }

        // 5. 删除申请记录
        demandApplyMapper.deleteById(demandApply.getId());
    }
}
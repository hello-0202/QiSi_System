package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.enums.MemberChangeStatusEnum;
import com.sc.qisi_system.common.enums.MemberChangeTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.DemandProgressCalculator;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import com.sc.qisi_system.module.practice.entity.DemandExecutionPlan;
import com.sc.qisi_system.module.practice.entity.DemandMemberChange;
import com.sc.qisi_system.module.practice.entity.DemandProgress;
import com.sc.qisi_system.module.practice.mapper.DemandExecutionPlanMapper;
import com.sc.qisi_system.module.practice.mapper.DemandMemberChangeMapper;
import com.sc.qisi_system.module.practice.mapper.DemandProgressMapper;
import com.sc.qisi_system.module.practice.service.PracticeExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 实践执行服务实现类
 */
@RequiredArgsConstructor
@Service
public class PracticeExecuteServiceImpl implements PracticeExecuteService {


    private final DemandProgressMapper demandProgressMapper;
    private final DemandExecutionPlanMapper demandExecutionPlanMapper;
    private final DemandMemberChangeMapper demandMemberChangeMapper;
    private final DemandService demandService;
    private final MinioService minioService;
    private final DemandProgressCalculator demandProgressCalculator;


    /**
     * 提交需求实践日志
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitDemandLog(DemandProgressDTO demandProgressDTO) {
        // 1. 校验需求是否存在
        if (!demandService.isNotExistsByDemandId(demandProgressDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 转换DTO为实体
        DemandProgress demandProgress = new DemandProgress();
        BeanUtils.copyProperties(demandProgressDTO, demandProgress);

        // 3. 插入进度日志
        demandProgressMapper.insert(demandProgress);
    }


    /**
     * 批量上传实践进度附件
     */
    @Override
    public AttachmentUploadVO batchUploadProgressAttachments(Long demandId, MultipartFile[] files) throws Exception {
        // 1. 校验需求是否存在
        if (demandService.isNotExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 调用MinIO服务上传附件
        return minioService.batchUploadProgressAttachments(demandId, files);
    }


    /**
     * 更新需求执行方案
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDemandPlan(DemandPlanDTO demandPlanDTO) {
        // 1. 查询需求执行方案
        DemandExecutionPlan demandExecutionPlan = demandExecutionPlanMapper.selectOne(Wrappers.lambdaQuery(DemandExecutionPlan.class)
                .eq(DemandExecutionPlan::getDemandId, demandPlanDTO.getDemandId()));
        if (demandExecutionPlan == null) {
            throw new BusinessException(ResultCode.DEMAND_PLAN_NOT_EXIST);
        }

        // 2. 更新方案内容与备注
        demandExecutionPlan.setResearchPlan(demandPlanDTO.getResearchPlan());
        demandExecutionPlan.setModifyRemark(demandPlanDTO.getModifyRemark());
        demandExecutionPlanMapper.updateById(demandExecutionPlan);

        // 3. 重新计算进度并更新需求
        Demand demand = demandService.getById(demandPlanDTO.getDemandId());
        demand.setProgressPercent(demandProgressCalculator.calculateProgress(demandExecutionPlan.getResearchPlan()));
        demandService.updateById(demand);
    }


    /**
     * 删除单个进度附件
     */
    @Override
    public void deleteProgressAttachment(Long attachmentId) {
        // 1. 调用MinIO服务删除附件
        minioService.deleteProgressAttachment(attachmentId);
    }


    /**
     * 批量删除进度附件
     */
    @Override
    public void deleteBatchProgressAttachment(List<Long> attachmentIds) {
        // 1. 调用MinIO服务批量删除附件
        minioService.deleteBatchProgressAttachment(attachmentIds);
    }


    /**
     * 申请退出需求实践
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyQuitDemand(MemberChangeDTO memberChangeDTO) {
        // 1. 校验需求是否存在
        if (demandService.isNotExistsByDemandId(memberChangeDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验是否重复提交退出申请
        long count = demandMemberChangeMapper.selectCount(Wrappers.lambdaQuery(DemandMemberChange.class)
                .eq(DemandMemberChange::getDemandId, memberChangeDTO.getDemandId())
                .eq(DemandMemberChange::getUserId, SecurityUtils.getCurrentUserId())
                .eq(DemandMemberChange::getStatus, MemberChangeStatusEnum.PENDING.getCode()));
        if (count > 0) {
            throw new BusinessException(ResultCode.DEMAND_QUIT_APPLIED_REPEAT);
        }

        // 3. 构建退出申请记录
        DemandMemberChange demandMemberChanged = new DemandMemberChange();
        BeanUtils.copyProperties(memberChangeDTO, demandMemberChanged);
        demandMemberChanged.setChangeType(MemberChangeTypeEnum.QUIT_APPLY.getCode());
        demandMemberChanged.setStatus(MemberChangeStatusEnum.PENDING.getCode());
        demandMemberChanged.setOperatorId(SecurityUtils.getCurrentUserId());

        // 4. 插入退出申请记录
        demandMemberChangeMapper.insert(demandMemberChanged);
    }
}
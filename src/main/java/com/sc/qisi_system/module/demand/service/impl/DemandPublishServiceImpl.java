package com.sc.qisi_system.module.demand.service.impl;

import com.sc.qisi_system.common.enums.CloseApplyStatusEnum;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandPublishService;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


/**
 * 需求发布服务实现类
 */
@RequiredArgsConstructor
@Service
public class DemandPublishServiceImpl implements DemandPublishService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final MinioService minioService;
    private final DemandService demandService;


    /**
     * 提交需求草稿
     */
    @Override
    public Long submitDraft(Long userId, DemandPublishDraftDTO demandPublishDraftDTO) {
        // 1. 校验用户是否存在
        if (sysUserService.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 转换DTO为实体对象
        Demand demand = new Demand();
        BeanUtils.copyProperties(demandPublishDraftDTO, demand);

        // 3. 设置草稿状态信息
        demand.setPublisherId(userId);
        demand.setStatus(DemandStatusEnum.DRAFT.getCode());
        demand.setCloseApplyStatus(CloseApplyStatusEnum.NO_APPLY.getCode());
        demand.setProgressPercent(0);

        // 4. 插入数据库
        demandMapper.insert(demand);

        return demand.getId();
    }


    /**
     * 更新需求草稿
     */
    @Override
    public Long updateDraft(DemandUpdateDraftDTO demandUpdateDraftDTO) {
        // 1. 校验需求ID是否为空
        if (demandUpdateDraftDTO.getId() == null) {
            throw new BusinessException(ResultCode.DEMAND_ID_NULL);
        }

        // 2. 查询需求信息
        Demand demand = demandMapper.selectById(demandUpdateDraftDTO.getId());
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 3. 校验是否为草稿状态
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.ONLY_ALLOW_DRAFT_EDIT);
        }

        // 4. 构建更新对象
        Demand updatedDemand = Demand.builder()
                .id(demandUpdateDraftDTO.getId())
                .publishType(demandUpdateDraftDTO.getPublishType())
                .title(demandUpdateDraftDTO.getTitle())
                .category(demandUpdateDraftDTO.getCategory())
                .researchField(demandUpdateDraftDTO.getResearchField())
                .background(demandUpdateDraftDTO.getBackground())
                .description(demandUpdateDraftDTO.getDescription())
                .techRequire(demandUpdateDraftDTO.getTechRequire())
                .startTime(demandUpdateDraftDTO.getStartTime())
                .endTime(demandUpdateDraftDTO.getEndTime())
                .researchCycle(demandUpdateDraftDTO.getResearchCycle())
                .maxMembers(demandUpdateDraftDTO.getMaxMembers())
                .requirePlan(demandUpdateDraftDTO.getRequirePlan())
                .progressPercent(0)
                .build();

        // 5. 执行更新
        demandMapper.updateById(updatedDemand);

        return demand.getId();
    }


    /**
     * 提交需求审核
     */
    @Override
    public Long submitAudit(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandMapper.selectById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验需求状态
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 3. 设置为审核中状态
        demand.setStatus(DemandStatusEnum.REVIEWING.getCode());
        demandMapper.updateById(demand);

        return demand.getId();
    }


    /**
     * 取消提交审核
     */
    @Override
    public Long cancelSubmit(Long userId, Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandMapper.selectById(demandId);

        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 校验操作权限
        if (!Objects.equals(demand.getPublisherId(), userId)) {
            throw new BusinessException(ResultCode.OPERATE_NOT_ALLOWED);
        }

        // 3. 校验是否为待审核状态
        if (!Objects.equals(demand.getStatus(), DemandStatusEnum.REVIEWING.getCode())) {
            throw new BusinessException(ResultCode.ONLY_REVIEWING_CANCEL);
        }

        // 4. 恢复为草稿状态
        demand.setStatus(DemandStatusEnum.DRAFT.getCode());
        demandMapper.updateById(demand);

        return demand.getId();
    }


    /**
     * 批量上传需求附件
     */
    @Override
    public AttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) throws Exception {
        // 1. 校验需求是否存在
        if (demandService.isNotExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 2. 调用MinIO服务上传文件
        return minioService.batchUploadDemandAttachment(demandId, files);
    }

    /**
     * 删除单个附件
     */
    @Override
    public void deleteAttachment(Long attachmentId) {
        minioService.deleteAttachment(attachmentId);
    }

    /**
     * 批量删除附件
     */
    @Override
    public void deleteBatchAttachment(List<Long> attachmentIds) {
        minioService.deleteBatchAttachment(attachmentIds);
    }
}
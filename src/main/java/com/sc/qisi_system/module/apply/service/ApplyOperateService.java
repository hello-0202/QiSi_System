package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.module.apply.dto.ApplyUpdateDTO;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;


/**
 * 需求申请操作服务接口
 * 功能: 需求认领申请提交、修改、取消等操作业务处理
 */
public interface ApplyOperateService {


    /**
     * 提交需求认领申请
     * 角色: 认领者
     *
     * @param userId 当前用户ID
     * @param demandApplyDTO 申请信息参数
     */
    void submitApply(Long userId, DemandApplyDTO demandApplyDTO);


    /**
     * 修改需求认领申请信息
     * 角色: 认领者
     *
     * @param userId 当前用户ID
     * @param applyUpdateDTO 修改信息参数
     */
    void updateApply(Long userId, ApplyUpdateDTO applyUpdateDTO);


    /**
     * 取消需求认领申请
     * 角色: 认领者
     *
     * @param userId 当前用户ID
     * @param demandId 需求ID
     */
    void cancelApply(Long userId, Long demandId);
}
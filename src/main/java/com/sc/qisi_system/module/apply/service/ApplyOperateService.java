package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.module.apply.dto.ApplyUpdateDTO;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;

public interface ApplyOperateService {


    /**
     * 提交需求申请
     *
     * @param demandApplyDTO 请求体
     */
    void submitApply(Long userId, DemandApplyDTO demandApplyDTO);


    /**
     * 修改申请信息
     *
     * @param applyUpdateDTO 请求体
     */
    void updateApply(Long userId, ApplyUpdateDTO applyUpdateDTO);


    /**
     * 取消认领申请
     *
     * @param demandId 需求id
     */
    void cancelApply(Long userId, Long demandId);

}




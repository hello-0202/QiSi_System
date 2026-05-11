package com.sc.qisi_system.module.apply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;

import java.util.Map;


/**
 * 需求申请服务接口
 * 功能: 需求申请基础操作、申请状态映射等业务处理
 */
public interface ApplyService extends IService<DemandApply> {


    /**
     * 获取用户申请状态映射表
     *
     * @param userId 用户id
     * @return 用户id和需求的映射表
     */
    Map<Long, DemandApplyList> getUserApplyMap(Long userId);
}
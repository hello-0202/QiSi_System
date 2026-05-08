package com.sc.qisi_system.module.apply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;

import java.util.Map;

public interface ApplyService extends IService<DemandApply> {


    /**
     * 获取映射表，判断改用户是否申请该需求
     *
     * @param userId 用户id
     * @return 用户id和需求的映射表
     */
    Map<Long, DemandApplyList> getUserApplyMap(Long userId);
}

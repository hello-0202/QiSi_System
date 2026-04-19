package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.module.apply.dto.ApplyUpdateDTO;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;

public interface ApplyOperateService {


    void submitApply(Long userId, DemandApplyDTO demandApplyDTO);


    void cancelApply(Long userId, Long demandId);


    void updateApply(Long userId, ApplyUpdateDTO applyUpdateDTO);
}

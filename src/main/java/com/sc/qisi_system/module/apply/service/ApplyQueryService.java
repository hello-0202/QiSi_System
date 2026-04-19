package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.demand.vo.DemandApplyListVO;

import java.util.Map;

public interface ApplyQueryService {


    ApplyDetailVO getApplyDetail(Long applyId);


    Map<Long, DemandApplyListVO> getUserApplyMap(Long userId);
}

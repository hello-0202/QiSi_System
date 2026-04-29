package com.sc.qisi_system.module.demand.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.vo.DemandListVO;

import java.util.Map;

public interface DemandService extends IService<Demand> {


    boolean notExistsByDemandId(Long demandId);

    PageResult<DemandListVO> convertToApplyPageResultList(IPage<Demand> demandIPage, Map<Long, DemandApplyList> applyStatusMap);

    PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO);
}

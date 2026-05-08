package com.sc.qisi_system.module.demand.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;

import java.util.Map;

public interface DemandService extends IService<Demand> {


    boolean isNotExistsByDemandId(Long demandId);


    PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO);


    PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    PageResult<DemandListVO> getMyJoinedPracticeList(Long userId, PracticeDemandQueryDTO queryDTO);


    Demand getDemand(Long demandId);


    /**
     * 查询可申请需求详情
     *
     * @param demandId 需求id
     * @return 需求详情列表
     */
    DemandPublicDetailVO getPublicDemandDetail(Long demandId);



    PageResult<DemandListVO> convertToApplyPageResultList(IPage<Demand> demandIPage, Map<Long, DemandApplyList> applyStatusMap);


    PageResult<DemandListVO> convertToPracticePageResultList(Long userId, IPage<Demand> demandIPage);


    PageResult<DemandListVO> convertToMyPageResultList(IPage<Demand> demandIPage);

    PageResult<DemandListVO> convertToAdminPageResultList(IPage<Demand> demandIPage);
}

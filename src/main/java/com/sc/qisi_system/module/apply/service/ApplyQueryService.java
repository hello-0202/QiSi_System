package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.apply.vo.ApplyMemberListVO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;

import java.util.List;
import java.util.Map;

public interface ApplyQueryService {


    /**
     * 查询申请详情
     *
     * @param applyId 需求申请id
     * @return 需求详情
     */
    ApplyDetailVO getMyApplyDetail(Long applyId);


    /**
     * 查看指定需求的申请成员列表
     *
     * @param userId 用户id
     * @param demandId 需求id
     */
    List<ApplyMemberListVO> getApplyMemberList(Long userId, Long demandId);


    /**
     * 查询申请成员的详细信息
     *
     * @param userId 用户id
     * @return 用户详情信息
     */
    ApplyMemberListVO getApplyMemberDetail(Long userId);


    ApplyDetailVO getApplyDetail(Long applyId);


    /**
     * 获取映射表，判断改用户是否申请该需求
     *
     * @param userId 用户id
     * @return 用户id和需求的映射表
     */
    Map<Long, DemandApplyList> getUserApplyMap(Long userId);
}

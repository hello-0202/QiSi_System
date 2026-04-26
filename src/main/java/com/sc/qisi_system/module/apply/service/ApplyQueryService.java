package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.apply.vo.ApplyMemberListVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;

import java.util.List;

public interface ApplyQueryService {


    /**
     * 条件查询我申请的需求列表
     *
     * @param userId 用户id
     * @param myApplyQueryDTO 请求体
     * @return 我申请的需求列表信息
     */
    PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO);


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


    /**
     * 查询指定成员的申请信息
     *
     * @param applyId 申请信息id
     * @return 申请详细信息
     */
    ApplyDetailVO getApplyDetail(Long applyId);



}

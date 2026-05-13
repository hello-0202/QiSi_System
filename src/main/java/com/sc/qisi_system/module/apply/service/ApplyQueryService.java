package com.sc.qisi_system.module.apply.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.demand.dto.ApplyDemandQueryDTO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;

import java.util.List;


/**
 * 需求申请查询服务接口
 * 功能: 我的申请列表、可申请需求、申请详情、成员信息等查询业务处理
 */
public interface ApplyQueryService {


    /**
     * 条件查询我申请的需求列表
     * 角色: 认领者
     *
     * @param userId 用户id
     * @param myApplyQueryDTO 请求体
     * @return 我申请的需求列表信息
     */
    PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO);


    /**
     * 查询公开可申请需求详情
     * 角色: 发布者 认领者
     *
     * @param demandId 需求ID
     * @return 公开需求详细信息
     */
    DemandPublicDetailVO getPublicDemandDetail(Long demandId);


    /**
     * 分页查询可申请需求列表
     * 角色: 认领者
     *
     * @param userId 用户id
     * @param queryDTO 查询条件
     * @return 可申请需求分页列表
     */
    PageResult<DemandListVO> getApplyList(Long userId, ApplyDemandQueryDTO queryDTO);


    /**
     * 查询申请详情
     * 角色: 认领者 发布者
     *
     * @param applyId 需求申请id
     * @return 需求详情
     */
    ApplyDetailVO getMyApplyDetail(Long applyId);


    /**
     * 查看指定需求的申请成员列表
     * 角色: 发布者
     *
     * @param userId 用户id
     * @param demandId 需求id
     * @return 申请成员列表信息
     */
    List<MemberVO> getApplyMemberList(Long userId, Long demandId);


    /**
     * 查询申请成员的详细信息
     * 角色: 发布者
     *
     * @param userId 用户id
     * @return 成员详情信息
     */
    MemberVO getApplyMemberDetailInfo(Long userId);


    /**
     * 查询指定成员的申请信息
     * 角色: 发布者
     *
     * @param applyId 申请信息id
     * @return 申请详细信息
     */
    ApplyDetailVO getApplyDetail(Long applyId);

}
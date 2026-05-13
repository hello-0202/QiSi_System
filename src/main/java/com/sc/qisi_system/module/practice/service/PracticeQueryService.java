package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.vo.AttachmentListVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.practice.dto.MemberChangeLogDTO;
import com.sc.qisi_system.module.practice.dto.QueryDemandProgressLogDTO;
import com.sc.qisi_system.module.practice.vo.DemandProgressVO;
import com.sc.qisi_system.module.practice.vo.MemberChangeLogVO;

import java.util.List;


/**
 * 实践查询业务服务接口
 * 提供需求查询、详情查看、成员查询、日志查询、附件查询等查询类操作
 */
public interface PracticeQueryService {


    /**
     * 实践中心查看我发布的需求列表
     * 角色: 发布者
     *
     * @param userId 用户ID
     * @param myDemandQueryDTO 查询请求体
     * @return 需求列表分页结果
     */
    PageResult<DemandListVO> getMyPublishedDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


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
     * 查询申请详情
     * 角色: 认领者 发布者
     *
     * @param applyId 需求申请id
     * @return 需求详情
     */
    ApplyDetailVO getMyApplyDetail(Long applyId);


    /**
     * 实践中心查看需求详情
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 需求详情VO
     */
    DemandPublicDetailVO getPracticeDemandDetail(Long demandId);


    /**
     * 查看我参与的实践需求列表
     * 角色: 认领者
     *
     * @param userId 用户ID
     * @param practiceDemandQueryDTO 分页查询请求体
     * @return 参与的需求列表分页结果
     */
    PageResult<DemandListVO> getMyJoinedPracticeList(Long userId, PracticeDemandQueryDTO practiceDemandQueryDTO);


    /**
     * 查询实践需求的进度附件列表
     * 角色: 发布者、认领者
     *
     * @param demandId 需求id
     * @return 附件列表
     */
    List<AttachmentListVO> getProgressAttachmentList(Long demandId);


    /**
     * 查询实践需求成员列表
     * 角色: 发布者、认领者
     *
     * @param demandId 需求id
     * @return 成员列表
     */
    List<MemberVO> getMemberList(Long demandId);


    /**
     * 查询需求成员变更记录
     * 角色: 发布者
     *
     * @param memberChangeLogDTO 分页查询请求体
     * @return 变更记录分页结果
     */
    PageResult<MemberChangeLogVO> getDemandMemberChangeLog(MemberChangeLogDTO memberChangeLogDTO);


    /**
     * 查询需求执行日志
     * 角色: 发布者、认领者
     *
     * @param queryDemandProgressLogDTO 日志查询请求体
     * @return 日志分页结果
     */
    PageResult<DemandProgressVO> getDemandProgressLog(QueryDemandProgressLogDTO queryDemandProgressLogDTO);
}
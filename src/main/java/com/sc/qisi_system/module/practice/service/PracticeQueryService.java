package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.common.result.PageResult;
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

public interface PracticeQueryService {


    /**
     * 实践中心查看需求列表
     * 角色: 发布者
     *
     * @param myDemandQueryDTO 查询请求体
     * @return 需求列表
     */
    PageResult<DemandListVO> getMyPublishedDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    /**
     * 实践中心查看需求详情(复用)
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 需求详情
     */
    DemandPublicDetailVO getPracticeDemandDetail(Long demandId);


    /**
     * 查看我参与的实践需求列表(复用)
     * 角色: 认领者
     *
     * @param practiceDemandQueryDTO 分页查询请求体
     * @return 我参与的需求列表
     */
    PageResult<DemandListVO> getMyJoinedPracticeList(Long userId,PracticeDemandQueryDTO practiceDemandQueryDTO);


    /**
     * 查询实践需求的附件列表
     * 角色: 发布者 认领者
     *
     * @param demandId 需求id
     * @return 需求附件列表
     */
    List<AttachmentListVO> getProgressAttachmentList(Long demandId);


    /**
     * 查询成员列表(复用)
     * 角色: 发布者 认领者
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
     * @return 变更记录
     */
    PageResult<MemberChangeLogVO> getDemandMemberChangeLog(MemberChangeLogDTO memberChangeLogDTO);


    /**
     * 查询需求日志
     *
     * @param queryDemandProgressLogDTO  需求日志查询请求体
     * @return 需求日志列表
     */
    PageResult<DemandProgressVO> getDemandProgressLog(QueryDemandProgressLogDTO queryDemandProgressLogDTO);
}

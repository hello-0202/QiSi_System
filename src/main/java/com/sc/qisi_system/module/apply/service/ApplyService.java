package com.sc.qisi_system.module.apply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.practice.vo.MemberVO;

import java.util.List;
import java.util.Map;


/**
 * 需求申请服务接口
 * 功能: 需求申请基础操作、申请状态映射等业务处理
 */
public interface ApplyService extends IService<DemandApply> {


    /**
     * 获取用户申请状态映射表
     *
     * @param userId 用户id
     * @return 用户id和需求的映射表
     */
    Map<Long, DemandApplyList> getUserApplyMap(Long userId);


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
}
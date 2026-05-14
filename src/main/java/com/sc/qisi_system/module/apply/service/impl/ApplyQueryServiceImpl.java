package com.sc.qisi_system.module.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.mapper.DemandApplyMapper;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import com.sc.qisi_system.module.apply.service.ApplyService;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.demand.dto.ApplyDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 需求申请查询服务实现类
 */
@RequiredArgsConstructor
@Service
public class ApplyQueryServiceImpl implements ApplyQueryService {


    private final DemandApplyMapper demandApplyMapper;
    private final DemandService demandService;
    private final ApplyService applyService;
    private final SysUserService sysUserService;
    private final MinioService minioService;


    /**
     * 条件查询我申请的需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO) {
        // 1. 设置分页查询
        Page<Demand> page = new Page<>(myApplyQueryDTO.getPageNum(), myApplyQueryDTO.getPageSize());

        // 2. 查询申请列表
        List<DemandApply> demandApplyList = applyService.list(
                new LambdaQueryWrapper<>(DemandApply.class)
                        .eq(DemandApply::getUserId, userId)
                        .eq(DemandApply::getAuditStatus, myApplyQueryDTO.getAuditStatus()));
        if (demandApplyList.isEmpty()) {
            PageResult<DemandListVO> empty = new PageResult<>();
            empty.setTotal(0L);
            empty.setRecords(Collections.emptyList());
            empty.setPages(0);
            return empty;
        }

        // 3. 查询需求列表
        LambdaQueryWrapper<Demand> demandQuery = new LambdaQueryWrapper<>();
        demandQuery
                .in(Demand::getId, demandApplyList.stream().map(DemandApply::getDemandId).toList())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandService.page(page, demandQuery);

        // 4. 转换并返回结果
        return demandService.convertToApplyPageResultList(demandIPage, applyService.getUserApplyMap(userId));
    }


    /**
     * 获取公开需求详情
     */
    @Override
    public DemandPublicDetailVO getPublicDemandDetail(Long demandId) {
        // 1. 判断需求是否存在，不存在则抛出异常
        if(!demandService.isNotExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        return demandService.getPublicDemandDetail(demandId);
    }


    /**
     * 分页查询可申请需求列表
     */
    @Override
    public PageResult<DemandListVO> getApplyList(Long userId, ApplyDemandQueryDTO queryDTO) {
        // 1. 设置分页查询
        Page<Demand> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 设置查询条件
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Demand::getStatus, DemandStatusEnum.PUBLISHED.getCode())
                .eq(queryDTO.getCategory() != null, Demand::getCategory, queryDTO.getCategory())
                .eq(queryDTO.getRequirePlan() != null, Demand::getRequirePlan, queryDTO.getRequirePlan())
                .ge(queryDTO.getDeadline() != null, Demand::getDeadline, queryDTO.getDeadline())
                .orderByDesc(Demand::getCreateTime);

        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        // 3. 转换并返回分页结果
        return demandService.convertToApplyPageResultList(demandIPage,applyService.getUserApplyMap(userId));
    }


    /**
     * 查询我的申请详情
     */
    @Override
    public ApplyDetailVO getMyApplyDetail(Long applyId) {
        return applyService.getMyApplyDetail(applyId);
    }


    /**
     * 查看指定需求的申请成员列表
     */
    @Override
    public List<MemberVO> getApplyMemberList(Long userId, Long demandId) {
        // 1. 校验需求是否存在
        if(!demandService.isNotExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        return applyService.getApplyMemberList(userId, demandId);
    }


    /**
     * 查询申请成员的详细信息
     */
    @Override
    public MemberVO getApplyMemberDetailInfo(Long userId) {
        // 1. 校验用户是否存在
        if(sysUserService.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 查询用户基础信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SysUser::getId, userId)
                .select(
                        SysUser::getPhone,
                        SysUser::getEmail,
                        SysUser::getAvatar
                );

        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 3. 封装成员详情VO
        MemberVO vo = new MemberVO();
        UserProfileVO userProfileVO = new UserProfileVO();
        userProfileVO.setPhone(sysUser.getPhone());
        userProfileVO.setEmail(sysUser.getEmail());
        vo.setUserProfileVO(userProfileVO);
        vo.setAvatarUrl(minioService.getUserAvatarUrl(sysUser.getAvatar()));

        return vo;
    }


    /**
     * 查询指定成员的申请信息
     */
    @Override
    public ApplyDetailVO getApplyDetail(Long applyId) {
        // 1. 根据ID查询申请记录
        DemandApply demandApply = demandApplyMapper.selectById(applyId);
        if(demandApply == null) {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        // 2. 转换为VO并返回
        ApplyDetailVO applyDetailVO = new ApplyDetailVO();
        BeanUtils.copyProperties(demandApply, applyDetailVO);

        return applyDetailVO;
    }
}
package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.mapper.DemandMemberMapper;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.demand.vo.AttachmentListVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.practice.dto.MemberChangeLogDTO;
import com.sc.qisi_system.module.practice.dto.QueryDemandProgressLogDTO;
import com.sc.qisi_system.module.practice.entity.DemandMemberChange;
import com.sc.qisi_system.module.practice.entity.DemandProgress;
import com.sc.qisi_system.module.practice.mapper.DemandMemberChangeMapper;
import com.sc.qisi_system.module.practice.mapper.DemandProgressMapper;
import com.sc.qisi_system.module.practice.service.PracticeQueryService;
import com.sc.qisi_system.module.practice.vo.DemandMemberVO;
import com.sc.qisi_system.module.practice.vo.DemandProgressVO;
import com.sc.qisi_system.module.practice.vo.MemberChangeLogVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * 实践查询服务实现类
 */
@RequiredArgsConstructor
@Service
public class PracticeQueryServiceImpl implements PracticeQueryService {


    private final DemandMemberChangeMapper demandMemberChangeMapper;
    private final DemandProgressMapper demandProgressMapper;
    private final DemandMemberMapper demandMemberMapper;
    private final DemandService demandService;
    private final SysUserService sysUserService;
    private final MinioService minioService;


    /**
     * 获取我发布的实践需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyPublishedDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {
        // 1. 定义允许查询的需求状态
        List<Integer> allowedStatus = Arrays.asList(
                DemandStatusEnum.PUBLISHED.getCode(),
                DemandStatusEnum.RESEARCHING.getCode(),
                DemandStatusEnum.COMPLETED.getCode(),
                DemandStatusEnum.CLOSED.getCode()
        );

        // 2. 处理状态参数，为空则默认查询全部允许状态
        List<Integer> statusList = Optional.ofNullable(myDemandQueryDTO.getStatusList()).orElse(new ArrayList<>());

        // 3. 校验状态合法性
        for (Integer status : statusList) {
            if (!allowedStatus.contains(status)) {
                throw new BusinessException(ResultCode.DEMAND_STATUS_ILLEGAL);
            }
        }

        if (statusList.isEmpty()) {
            myDemandQueryDTO.setStatusList(allowedStatus);
        }

        // 4. 调用服务查询并返回
        return demandService.getMyDemandList(userId, myDemandQueryDTO);
    }


    /**
     * 获取实践需求详情
     */
    @Override
    public DemandPublicDetailVO getPracticeDemandDetail(Long demandId) {
        // 1. 查询公开需求详情
        return demandService.getPublicDemandDetail(demandId);
    }


    /**
     * 获取我参与的实践需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyJoinedPracticeList(Long userId, PracticeDemandQueryDTO practiceDemandQueryDTO) {
        // 1. 查询参与的实践需求
        return demandService.getMyJoinedPracticeList(userId, practiceDemandQueryDTO);
    }


    /**
     * 获取实践进度附件列表
     */
    @Override
    public List<AttachmentListVO> getProgressAttachmentList(Long demandId) {
        // 1. 查询进度附件
        return minioService.getProgressAttachmentList(demandId);
    }


    /**
     * 获取需求成员列表
     */
    @Override
    public List<MemberVO> getMemberList(Long demandId) {
        // 1. 查询需求成员
        LambdaQueryWrapper<DemandMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandMember::getDemandId, demandId);
        List<DemandMember> demandMemberList = demandMemberMapper.selectList(queryWrapper);

        // 2. 转换为VO并填充用户信息
        return demandMemberList.stream().map(
                demandMember -> {
                    MemberVO memberVO = new MemberVO();
                    DemandMemberVO demandMemberVO = new DemandMemberVO();
                    BeanUtils.copyProperties(demandMember, demandMemberVO);
                    memberVO.setDemandMemberVO(demandMemberVO);

                    BeanUtils.copyProperties(sysUserService.getUserProfile(demandMemberVO.getUserId()), memberVO.getUserProfileVO());

                    return memberVO;
                }).toList();
    }


    /**
     * 分页查询需求成员变更日志
     */
    @Override
    public PageResult<MemberChangeLogVO> getDemandMemberChangeLog(MemberChangeLogDTO memberChangeLogDTO) {
        // 1. 构建分页对象
        Page<DemandMemberChange> page = new Page<>(memberChangeLogDTO.getPageNum(), memberChangeLogDTO.getPageSize());

        // 2. 校验需求是否存在
        if (!demandService.isNotExistsByDemandId(memberChangeLogDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 3. 分页查询变更记录
        LambdaQueryWrapper<DemandMemberChange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandMemberChange::getDemandId, memberChangeLogDTO.getDemandId());
        IPage<DemandMemberChange> demandMemberChangeIPage = demandMemberChangeMapper.selectPage(page, queryWrapper);

        // 4. 转换为VO并填充用户信息
        List<MemberChangeLogVO> voList = demandMemberChangeIPage.getRecords().stream().map(
                memberChange -> {
                    MemberChangeLogVO memberChangeLogVO = new MemberChangeLogVO();
                    BeanUtils.copyProperties(memberChange, memberChangeLogVO);
                    UserProfileVO userProfileVO = sysUserService.getUserProfile(memberChange.getUserId());
                    BeanUtils.copyProperties(userProfileVO, memberChangeLogVO.getUserProfileVO());
                    return memberChangeLogVO;
                }).toList();

        // 5. 封装分页结果
        PageResult<MemberChangeLogVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandMemberChangeIPage.getTotal());
        pageResult.setPages(demandMemberChangeIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }


    /**
     * 分页查询需求实践日志
     */
    @Override
    public PageResult<DemandProgressVO> getDemandProgressLog(QueryDemandProgressLogDTO queryDemandProgressLogDTO) {
        // 1. 构建分页对象
        Page<DemandProgress> page = new Page<>(queryDemandProgressLogDTO.getPageNum(), queryDemandProgressLogDTO.getPageSize());

        // 2. 校验需求是否存在
        if (demandService.isNotExistsByDemandId(queryDemandProgressLogDTO.getDemandId())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        // 3. 分页查询进度日志
        LambdaQueryWrapper<DemandProgress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandProgress::getDemandId, queryDemandProgressLogDTO.getDemandId());
        IPage<DemandProgress> demandProgressIPage = demandProgressMapper.selectPage(page, queryWrapper);

        // 4. 转换为VO并填充用户名
        List<DemandProgressVO> demandProgressVOList = demandProgressIPage.getRecords().stream().map(
                demandProgress -> {
                    DemandProgressVO demandProgressVO = new DemandProgressVO();
                    BeanUtils.copyProperties(demandProgress, demandProgressVO);

                    SysUser sysUser = sysUserService.getOne(Wrappers.lambdaQuery(SysUser.class)
                            .eq(SysUser::getId, demandProgress.getUserId())
                            .select(SysUser::getName));
                    if (sysUser != null) {
                        demandProgressVO.setName(sysUser.getName());
                    }

                    return demandProgressVO;
                }).toList();

        // 5. 封装分页结果
        PageResult<DemandProgressVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandProgressIPage.getTotal());
        pageResult.setPages(demandProgressIPage.getPages());
        pageResult.setRecords(demandProgressVOList);

        return pageResult;
    }
}
package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import com.sc.qisi_system.module.apply.service.ApplyService;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.domain.DemandPracticeList;
import com.sc.qisi_system.module.demand.domain.DemandPublisherBase;
import com.sc.qisi_system.module.demand.domain.DemandPublisherList;
import com.sc.qisi_system.module.demand.dto.ApplicableDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.vo.*;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import com.sc.qisi_system.module.user.entity.EduStudent;
import com.sc.qisi_system.module.user.entity.EduTeacher;
import com.sc.qisi_system.module.user.entity.EntEmployee;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.EduStudentService;
import com.sc.qisi_system.module.user.service.EduTeacherService;
import com.sc.qisi_system.module.user.service.EntEmployeeService;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DemandQueryServiceImpl implements DemandQueryService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final EduTeacherService eduTeacherService;
    private final EduStudentService eduStudentService;
    private final EntEmployeeService entEmployeeService;
    private final ApplyService applyService;
    private final ApplyQueryService applyQueryService;
    private final DemandMemberService demandMemberService;


    @Override
    public PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize) {

        Page<Demand> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
                .eq(Demand::getPublisherId, userId)
                .eq(Demand::getStatus, DemandStatusEnum.DRAFT.getCode())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        return convertToMyPageResultList(demandIPage);
    }


    @Override
    public PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {

        Page<Demand> page = new Page<>(myDemandQueryDTO.getPageNum(), myDemandQueryDTO.getPageSize());
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Demand::getPublisherId, userId)
                .gt(Demand::getStatus, DemandStatusEnum.DRAFT.getCode());

        if (myDemandQueryDTO.getCreateTime() != null) {
            queryWrapper.ge(Demand::getCreateTime, myDemandQueryDTO.getCreateTime());
        }
        if (CollectionUtils.isNotEmpty(myDemandQueryDTO.getStatusList())) {
            queryWrapper.in(Demand::getStatus, myDemandQueryDTO.getStatusList());
        }
        queryWrapper.orderByDesc(Demand::getCreateTime)
                .orderByDesc(Demand::getProgressPercent);

        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        return convertToMyPageResultList(demandIPage);
    }


    @Override
    public MyDemandDetailVO getMyDemandDetail(Long demandId) {

        Demand demand = getDemand(demandId);

        MyDemandDetailVO myDemandDetailVO = new MyDemandDetailVO();
        BeanUtils.copyProperties(demand, myDemandDetailVO);
        BeanUtils.copyProperties(getDemandUserBase(demand.getPublisherId()), myDemandDetailVO.getDemandPublisherDetailVO());

        return myDemandDetailVO;
    }


    @Override
    public PageResult<DemandListVO> getApplicableList(Long userId,ApplicableDemandQueryDTO queryDTO) {

        // 1. 设置分页拆查询
        Page<Demand> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 设置查询条件
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Demand::getStatus, DemandStatusEnum.PUBLISHED.getCode())
                .eq(queryDTO.getCategory() != null, Demand::getCategory, queryDTO.getCategory())
                .eq(queryDTO.getRequirePlan() != null, Demand::getRequirePlan, queryDTO.getRequirePlan())
                .ge(queryDTO.getDeadline() != null, Demand::getDeadline, queryDTO.getDeadline())
                .orderByDesc(Demand::getCreateTime);

        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        // 3. 返回包装
        return convertToApplicablePageResultList(demandIPage,applyQueryService.getUserApplyMap(userId));
    }


    @Override
    public DemandReceiverDetailVO getDemandReceiverDetail(Long demandId) {

        Demand demand = getDemand(demandId);

        if (Objects.equals(demand.getStatus(), DemandStatusEnum.PUBLISHED.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_PUBLISHED);
        }

        DemandReceiverDetailVO demandReceiverDetailVO = new DemandReceiverDetailVO();
        BeanUtils.copyProperties(demand, demandReceiverDetailVO);
        BeanUtils.copyProperties(getDemandUserBase(demand.getPublisherId()), demandReceiverDetailVO.getDemandPublisherDetailVO());

        return demandReceiverDetailVO;
    }


    @Override
    public PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO) {

        // 1. 设置分页查询
        Page<Demand> page = new Page<>(myApplyQueryDTO.getPageNum(), myApplyQueryDTO.getPageSize());

        // 2. 查询申请列表
        List<DemandApply> demandApplyList = applyService
                .list(new LambdaQueryWrapper<>(DemandApply.class)
                        .eq(DemandApply::getUserId,userId));
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
                .in(Demand::getId,demandApplyList.stream().map(DemandApply::getDemandId).toList())
                .orderByDesc(Demand::getCreateTime);
        IPage<Demand> demandIPage = demandMapper.selectPage(page, demandQuery);

        return convertToApplicablePageResultList(demandIPage,applyQueryService.getUserApplyMap(userId));
    }


    @Override
    public PageResult<DemandListVO> getMyPracticeDemandList(Long userId, PracticeDemandQueryDTO queryDTO) {

        // 1. 设置分页拆查询
        Page<Demand> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 设置查询条件
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .in(Demand::getStatus,queryDTO.getStatusList())
                .orderByDesc(Demand::getCreateTime);

        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        // 3. 返回包装
        return convertToPracticePageResultList(userId,demandIPage);
    }


    /**
     *
     */
    private Demand getDemand(Long demandId) {

        Demand demand = demandMapper.selectById(demandId);
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        return demand;
    }


    /**
     *
     */
    private PageResult<DemandListVO> convertToMyPageResultList(IPage<Demand> demandIPage) {

        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {

                    DemandPublisherBase demandPublisherBase = getDemandUserBase(demand.getPublisherId());
                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);
                    DemandPublisherList demandPublisherList = new DemandPublisherList();
                    BeanUtils.copyProperties(demandPublisherBase, demandPublisherList);
                    vo.setDemandPublisherList(demandPublisherList);



                    return vo;

                }).toList();

        PageResult<DemandListVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandIPage.getTotal());
        pageResult.setPages(demandIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }


    /**
     *
     */
    private PageResult<DemandListVO> convertToApplicablePageResultList(IPage<Demand> demandIPage,Map<Long, DemandApplyList> applyStatusMap) {

        // 1. 转换VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {

                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);

                    // 2. 设置发布人信息
                    DemandPublisherBase userBase = getDemandUserBase(demand.getPublisherId());
                    DemandPublisherList userListVO = new DemandPublisherList();
                    BeanUtils.copyProperties(userBase, userListVO);
                    vo.setDemandPublisherList(userListVO);
                    DemandApplyList applyListVO = new DemandApplyList();

                    // 3. 设置申请信息
                    applyListVO.setId(applyStatusMap.get(demand.getId()).getId());
                    applyListVO.setAuditStatus(applyStatusMap.get(demand.getId()).getAuditStatus());
                    vo.setDemandApplyList(applyListVO);

                    return vo;
                }).toList();

        // 3. 返回包装
        PageResult<DemandListVO> result = new PageResult<>();
        result.setTotal(demandIPage.getTotal());
        result.setPages(demandIPage.getPages());
        result.setRecords(voList);
        return result;
    }


    /**
     *
     */
    private PageResult<DemandListVO> convertToPracticePageResultList(Long userId,IPage<Demand> demandIPage) {

        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {

                    DemandPublisherBase demandPublisherBase = getDemandUserBase(demand.getPublisherId());
                    DemandListVO vo = new DemandListVO();
                    BeanUtils.copyProperties(demand, vo);
                    DemandPublisherList demandPublisherList = new DemandPublisherList();
                    BeanUtils.copyProperties(demandPublisherBase, demandPublisherList);
                    vo.setDemandPublisherList(demandPublisherList);

                    LambdaQueryWrapper<DemandMember> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper
                            .eq(DemandMember::getDemandId,demand.getId())
                            .eq(DemandMember::getUserId,userId);
                    DemandMember demandMember = demandMemberService.getOne(queryWrapper);
                    DemandPracticeList demandPracticeList = new DemandPracticeList();
                    if (demandMember != null) {
                        BeanUtils.copyProperties(demandMember, demandPracticeList);
                    }
                    vo.setDemandPracticeList(demandPracticeList);

                    return vo;

                }).toList();

        PageResult<DemandListVO> pageResult = new PageResult<>();
        pageResult.setTotal(demandIPage.getTotal());
        pageResult.setPages(demandIPage.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }


    /**
     *
     */
    private DemandPublisherBase getDemandUserBase(Long userId) {

        SysUser sysUser = sysUserService.getById(userId);
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        Integer userType = sysUser.getUserType();

        DemandPublisherBase demandPublisherBase = new DemandPublisherBase();
        BeanUtils.copyProperties(sysUser, demandPublisherBase);

        return switch (userType) {
            case 1 -> getStudentDetail(sysUser, demandPublisherBase);
            case 2 -> getTeacherDetail(sysUser, demandPublisherBase);
            case 3 -> getEmployeeDetail(sysUser, demandPublisherBase);
            default -> demandPublisherBase;
        };

    }


    /**
     *
     */
    private DemandPublisherBase getEmployeeDetail(SysUser sysUser, DemandPublisherBase baseVO) {

        EntEmployee emp = entEmployeeService.getOne(Wrappers
                .lambdaQuery(EntEmployee.class)
                .eq(EntEmployee::getUserId, sysUser.getId()));

        if (Objects.nonNull(emp)) BeanUtils.copyProperties(emp, baseVO);
        return baseVO;
    }


    /**
     *
     */
    private DemandPublisherBase getTeacherDetail(SysUser sysUser, DemandPublisherBase baseVO) {

        EduTeacher teacher = eduTeacherService.getOne(Wrappers
                .lambdaQuery(EduTeacher.class)
                .eq(EduTeacher::getUserId, sysUser.getId()));

        if (Objects.nonNull(teacher)) BeanUtils.copyProperties(teacher, baseVO);
        return baseVO;
    }


    /**
     *
     */
    private DemandPublisherBase getStudentDetail(SysUser sysUser, DemandPublisherBase baseVO) {

        EduStudent student = eduStudentService.getOne(Wrappers
                .lambdaQuery(EduStudent.class)
                .eq(EduStudent::getUserId, sysUser.getId()));

        if (Objects.nonNull(student)) BeanUtils.copyProperties(student, baseVO);
        return baseVO;
    }

}

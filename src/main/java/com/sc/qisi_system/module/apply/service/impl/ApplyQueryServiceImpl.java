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
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.user.domain.UserInfoBase;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ApplyQueryServiceImpl implements ApplyQueryService {


    private final DemandApplyMapper demandApplyMapper;
    private final DemandService demandService;
    private final ApplyService applyService;
    private final SysUserService sysUserService;
    private final EduStudentService eduStudentService;
    private final EduTeacherService eduTeacherService;
    private final EntEmployeeService entEmployeeService;


    @Override
    public PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO) {
        return demandService.getMyApplyDemandList(userId, myApplyQueryDTO);
    }

    @Override
    public PageResult<DemandListVO> getApplyList(Long userId, ApplyDemandQueryDTO queryDTO) {

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

        IPage<Demand> demandIPage = demandService.page(page, queryWrapper);

        // 3. 返回包装
        return demandService.convertToApplyPageResultList(demandIPage,applyService.getUserApplyMap(userId));
    }


    @Override
    public ApplyDetailVO getMyApplyDetail(Long applyId) {

        DemandApply demandApply = demandApplyMapper.selectById(applyId);

        if(demandApply == null) {
            throw new BusinessException( ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        ApplyDetailVO applyDetailVO = new ApplyDetailVO();
        BeanUtils.copyProperties(demandApply, applyDetailVO);

        return applyDetailVO;
    }


    @Override
    public List<MemberVO> getApplyMemberList(Long userId, Long demandId) {

        if(!demandService.notExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }
        if(sysUserService.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 1. 查询申请列表
        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandApply::getDemandId, demandId)
                .select(
                        DemandApply::getId,
                        DemandApply::getUserId
                );
        List<DemandApply> demandApplyList = demandApplyMapper.selectList(queryWrapper);

        // 2. 构建 userId -> applyId 的映射
        Map<Long, Long> userIdToApplyIdMap = demandApplyList.stream()
                .collect(Collectors.toMap(
                        DemandApply::getUserId,
                        DemandApply::getId
                ));

        // 3. 提取所有用户ID
        List<Long> userIds = demandApplyList.stream()
                .map(DemandApply::getUserId)
                .filter(Objects::nonNull)
                .toList();

        if (userIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 4. 查询用户信息
        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.in(SysUser::getId, userIds)
                .select(
                        SysUser::getId,
                        SysUser::getName,
                        SysUser::getAvatar,
                        SysUser::getUserType
                );
        List<SysUser> sysUserList = sysUserService.list(userQueryWrapper);

        // 5. 封装VO
        List<MemberVO> voList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            MemberVO vo = new MemberVO();
            vo.setId(sysUser.getId());
            vo.setApplyId(userIdToApplyIdMap.get(sysUser.getId()));

            UserInfoBase userInfoBase = new UserInfoBase();
            userInfoBase.setName(sysUser.getName());
            userInfoBase.setAvatar(sysUser.getAvatar());
            userInfoBase.setUserType(sysUser.getUserType());
            vo.setUserInfoBase(userInfoBase);

            loadUserInfoByRole(sysUser.getId(), sysUser.getUserType(), vo);
            voList.add(vo);
        }

        return voList;
    }


    @Override
    public MemberVO getMemberDetail(Long userId) {
        if(sysUserService.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SysUser::getId, userId)
                .select(
                        SysUser::getPhone,
                        SysUser::getEmail
                );

        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        MemberVO vo = new MemberVO();
        vo.getUserInfoBase().setPhone(sysUser.getPhone());
        vo.getUserInfoBase().setEmail(sysUser.getEmail());

        return vo;
    }


    @Override
    public ApplyDetailVO getApplyDetail(Long applyId) {

        DemandApply demandApply = demandApplyMapper.selectById(applyId);
        if(demandApply == null) {
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }
        ApplyDetailVO applyDetailVO = new ApplyDetailVO();
        BeanUtils.copyProperties(demandApply, applyDetailVO);

        return applyDetailVO;
    }


    private void loadUserInfoByRole(Long userId, Integer userType, MemberVO vo) {

        switch (userType) {
            case 1 -> fillStudentListInfo(userId, vo);
            case 2 -> fillTeacherListInfo(userId, vo);
            case 3 -> fillEnterpriseListInfo(userId, vo);
        }
    }


    /**
     * 填充：学生拓展信息
     */
    private void fillStudentListInfo(Long userId, MemberVO vo) {
        LambdaQueryWrapper<EduStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduStudent::getUserId, userId)
                .select(
                        EduStudent::getCollege,
                        EduStudent::getMajor,
                        EduStudent::getClassName,
                        EduStudent::getGrade
                );
        EduStudent eduStudent = eduStudentService.getOne(queryWrapper);

        if (eduStudent == null) {
            return;
        }
        BeanUtils.copyProperties(eduStudent, vo.getUserInfoBase());
    }


    /**
     * 填充：教师拓展信息
     */
    private void fillTeacherListInfo(Long userId, MemberVO vo) {
        LambdaQueryWrapper<EduTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(EduTeacher::getUserId, userId)
                .select(EduTeacher::getUnitName);
        EduTeacher eduTeacher = eduTeacherService.getOne(queryWrapper);

        if (eduTeacher == null) {
            return;
        }
        BeanUtils.copyProperties(eduTeacher, vo.getUserInfoBase());
    }


    /**
     * 填充：企业人员拓展信息
     */
    private void fillEnterpriseListInfo(Long userId, MemberVO vo) {
        LambdaQueryWrapper<EntEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(EntEmployee::getUserId, userId)
                .select(EntEmployee::getEnterpriseName);
        EntEmployee entEmployee = entEmployeeService.getOne(queryWrapper);
        if (entEmployee == null) {
            return;
        }
        BeanUtils.copyProperties(entEmployee, vo.getUserInfoBase());
    }
}

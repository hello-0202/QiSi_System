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
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
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
    private final EduStudentService eduStudentService;
    private final EduTeacherService eduTeacherService;
    private final EntEmployeeService entEmployeeService;
    private final MinioService minioService;


    /**
     * 条件查询我申请的需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO) {
        return demandService.getMyApplyDemandList(userId, myApplyQueryDTO);
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
        // 1. 根据ID查询申请记录
        DemandApply demandApply = demandApplyMapper.selectById(applyId);

        // 2. 校验申请记录是否存在
        if(demandApply == null) {
            throw new BusinessException( ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        // 3. 转换为VO并返回
        ApplyDetailVO applyDetailVO = new ApplyDetailVO();
        BeanUtils.copyProperties(demandApply, applyDetailVO);

        return applyDetailVO;
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
        if(sysUserService.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 查询申请列表
        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandApply::getDemandId, demandId)
                .select(
                        DemandApply::getId,
                        DemandApply::getUserId
                );
        List<DemandApply> demandApplyList = demandApplyMapper.selectList(queryWrapper);

        // 3. 构建 userId -> applyId 的映射
        Map<Long, Long> userIdToApplyIdMap = demandApplyList.stream()
                .collect(Collectors.toMap(
                        DemandApply::getUserId,
                        DemandApply::getId
                ));

        // 4. 提取所有用户ID
        List<Long> userIds = demandApplyList.stream()
                .map(DemandApply::getUserId)
                .filter(Objects::nonNull)
                .toList();

        if (userIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 5. 查询用户信息
        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.in(SysUser::getId, userIds)
                .select(
                        SysUser::getId,
                        SysUser::getName,
                        SysUser::getAvatar,
                        SysUser::getUserType
                );
        List<SysUser> sysUserList = sysUserService.list(userQueryWrapper);

        // 6. 封装VO
        List<MemberVO> voList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            MemberVO vo = new MemberVO();
            vo.setId(sysUser.getId());
            vo.setApplyId(userIdToApplyIdMap.get(sysUser.getId()));
            vo.setAvatarUrl(minioService.getUserAvatarUrl(sysUser.getAvatar()));

            UserProfileVO userProfileVO = new UserProfileVO();
            userProfileVO.setName(sysUser.getName());
            userProfileVO.setAvatar(sysUser.getAvatar());
            userProfileVO.setUserType(sysUser.getUserType());
            vo.setUserProfileVO(userProfileVO);

            loadUserInfoByRole(sysUser.getId(), sysUser.getUserType(), vo);
            voList.add(vo);
        }

        return voList;
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


    /**
     * 根据用户类型加载对应用户信息
     */
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
        // 1. 查询学生信息
        LambdaQueryWrapper<EduStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduStudent::getUserId, userId)
                .select(
                        EduStudent::getCollege,
                        EduStudent::getMajor,
                        EduStudent::getClassName,
                        EduStudent::getGrade
                );
        EduStudent eduStudent = eduStudentService.getOne(queryWrapper);

        // 2. 赋值到VO
        if (eduStudent == null) {
            return;
        }
        BeanUtils.copyProperties(eduStudent, vo.getUserProfileVO());
    }


    /**
     * 填充：教师拓展信息
     */
    private void fillTeacherListInfo(Long userId, MemberVO vo) {
        // 1. 查询教师信息
        LambdaQueryWrapper<EduTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(EduTeacher::getUserId, userId)
                .select(EduTeacher::getUnitName);
        EduTeacher eduTeacher = eduTeacherService.getOne(queryWrapper);

        // 2. 赋值到VO
        if (eduTeacher == null) {
            return;
        }
        BeanUtils.copyProperties(eduTeacher, vo.getUserProfileVO());
    }


    /**
     * 填充：企业人员拓展信息
     */
    private void fillEnterpriseListInfo(Long userId, MemberVO vo) {
        // 1. 查询企业人员信息
        LambdaQueryWrapper<EntEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(EntEmployee::getUserId, userId)
                .select(EntEmployee::getEnterpriseName);
        EntEmployee entEmployee = entEmployeeService.getOne(queryWrapper);

        // 2. 赋值到VO
        if (entEmployee == null) {
            return;
        }
        BeanUtils.copyProperties(entEmployee, vo.getUserProfileVO());
    }
}
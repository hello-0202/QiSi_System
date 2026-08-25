package com.sc.qisi_system.module.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.mapper.DemandApplyMapper;
import com.sc.qisi_system.module.apply.service.ApplyService;
import com.sc.qisi_system.module.apply.vo.ApplyDetailVO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import com.sc.qisi_system.module.practice.vo.DemandMemberVO;
import com.sc.qisi_system.module.practice.vo.MemberVO;
import com.sc.qisi_system.module.user.entity.EduStudent;
import com.sc.qisi_system.module.user.entity.EduTeacher;
import com.sc.qisi_system.module.user.entity.EntEmployee;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.EduStudentService;
import com.sc.qisi_system.module.user.service.EduTeacherService;
import com.sc.qisi_system.module.user.service.EntEmployeeService;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 需求申请服务实现类
 */
@RequiredArgsConstructor
@Service
public class ApplyServiceImpl extends ServiceImpl<DemandApplyMapper,DemandApply> implements ApplyService {


    private final DemandApplyMapper demandApplyMapper;
    private final DemandMemberService demandMemberService;
    private final SysUserService sysUserService;
    private final EduStudentService eduStudentService;
    private final EduTeacherService eduTeacherService;
    private final EntEmployeeService entEmployeeService;
    private final MinioService minioService;


    /**
     * 获取用户申请状态映射表
     */
    @Override
    public Map<Long, DemandApplyList> getUserApplyMap(Long userId) {
        // 1. 查询当前用户所有需求申请记录
        List<DemandApply> applyList = demandApplyMapper.selectList(new LambdaQueryWrapper<DemandApply>()
                .eq(DemandApply::getUserId, userId));

        // 2. 转换为需求ID -> 申请信息的Map结构并返回
        return applyList.stream()
                .collect(Collectors.toMap(
                        DemandApply::getDemandId,
                        apply -> {
                            DemandApplyList vo = new DemandApplyList();
                            vo.setId(apply.getId());
                            vo.setAuditStatus(apply.getAuditStatus());
                            return vo;
                        }
                ));
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
            throw new BusinessException(ResultCode.DEMAND_APPLY_NOT_EXIST);
        }

        // 3. 转换为VO并返回
        ApplyDetailVO applyDetailVO = new ApplyDetailVO();
        BeanUtils.copyProperties(demandApply, applyDetailVO);
        applyDetailVO.setName(sysUserService.selectUserNameById(demandApply.getUserId()));

        return applyDetailVO;
    }


    /**
     * 查看指定需求的申请成员列表
     */
    @Override
    public List<MemberVO> getApplyMemberList(Long userId, Long demandId) {
        // 修复BUG：用户不存在才抛异常，原来逻辑写反
        if (sysUserService.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 查询当前需求下所有申请记录
        LambdaQueryWrapper<DemandApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandApply::getDemandId, demandId)
                .select(
                        DemandApply::getId,
                        DemandApply::getUserId,
                        DemandApply::getAuditStatus // 新增：查询审核状态字段
                );
        List<DemandApply> demandApplyList = demandApplyMapper.selectList(queryWrapper);

        // 3. 构建 userId -> applyId 映射 + userId -> auditStatus 审核状态映射
        Map<Long, Long> userIdToApplyIdMap = new HashMap<>();
        Map<Long, Integer> userIdToAuditStatusMap = new HashMap<>();
        for (DemandApply apply : demandApplyList) {
            userIdToApplyIdMap.put(apply.getUserId(), apply.getId());
            userIdToAuditStatusMap.put(apply.getUserId(), apply.getAuditStatus());
        }

        // 4. 提取所有用户ID
        List<Long> userIds = demandApplyList.stream()
                .map(DemandApply::getUserId)
                .filter(Objects::nonNull)
                .toList();

        if (userIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 4.5 批量查询该需求下所有DemandMember，避免循环查库
        LambdaQueryWrapper<DemandMember> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(DemandMember::getDemandId, demandId);
        List<DemandMember> allMemberList = demandMemberService.list(memberWrapper);
        // key:userId value:成员实体
        Map<Long, DemandMember> userIdToMemberMap = allMemberList.stream()
                .collect(Collectors.toMap(
                        DemandMember::getUserId,
                        Function.identity(),
                        (oldVal, newVal) -> oldVal // 同一用户多条成员记录保留第一条
                ));

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
            Long uid = sysUser.getId();

            // 从预加载Map直接获取成员数据，不再循环查询数据库
            DemandMember demandMember = userIdToMemberMap.get(uid);

            MemberVO vo = new MemberVO();
            vo.setId(uid);
            vo.setApplyId(userIdToApplyIdMap.get(uid));
            // 填充审核状态auditStatus
            vo.setAuditStatus(userIdToAuditStatusMap.get(uid));
            vo.setAvatarUrl(minioService.getUserAvatarUrl(sysUser.getAvatar()));

            // 封装用户基础信息VO
            UserProfileVO userProfileVO = new UserProfileVO();
            userProfileVO.setId(uid);
            userProfileVO.setName(sysUser.getName());
            userProfileVO.setAvatar(minioService.getUserAvatarUrl(sysUser.getAvatar()));
            userProfileVO.setUserType(sysUser.getUserType());
            vo.setUserProfileVO(userProfileVO);

            // 封装成员VO
            DemandMemberVO demandMemberVO = null;
            if (demandMember != null) {
                demandMemberVO = new DemandMemberVO();
                BeanUtils.copyProperties(demandMember, demandMemberVO);
            }
            vo.setDemandMemberVO(demandMemberVO);

            loadUserInfoByRole(uid, sysUser.getUserType(), vo);
            voList.add(vo);
        }

        return voList;
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
        BeanUtils.copyProperties(eduStudent, vo.getUserProfileVO(),"name", "avatarUrl", "userType","id");
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
        BeanUtils.copyProperties(eduTeacher, vo.getUserProfileVO(),"name", "avatarUrl", "userType","id");
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
        BeanUtils.copyProperties(entEmployee, vo.getUserProfileVO(),"name", "avatarUrl", "userType","id");
    }
}
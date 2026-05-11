package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.common.enums.DemandStatusEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.entity.DemandApply;
import com.sc.qisi_system.module.apply.service.ApplyService;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.domain.DemandPracticeList;
import com.sc.qisi_system.module.demand.domain.DemandPublisherList;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 需求业务服务实现类
 */
@RequiredArgsConstructor
@Service
public class DemandServiceImpl extends ServiceImpl<DemandMapper, Demand> implements DemandService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final ApplyService applyService;
    private final DemandMemberService demandMemberService;
    private final MinioService minioService;


    /**
     * 判断需求是否存在
     */
    @Override
    public boolean isNotExistsByDemandId(Long demandId) {
        return getById(demandId) != null;
    }


    /**
     * 获取我申请的需求列表
     */
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

        // 4. 转换并返回结果
        return convertToApplyPageResultList(demandIPage,applyService.getUserApplyMap(userId));
    }


    /**
     * 获取我发布的需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {
        // 1. 构建分页和查询条件
        Page<Demand> page = new Page<>(myDemandQueryDTO.getPageNum(), myDemandQueryDTO.getPageSize());
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        // 2. 设置查询条件
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

        // 3. 分页查询并转换结果
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);
        return convertToMyPageResultList(demandIPage);
    }


    /**
     * 获取我参与的实践需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyJoinedPracticeList(Long userId, PracticeDemandQueryDTO queryDTO) {
        // 1. 设置分页查询
        Page<Demand> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 设置查询条件
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .in(Demand::getStatus,queryDTO.getStatusList())
                .orderByDesc(Demand::getCreateTime);

        // 3. 分页查询数据
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);

        // 4. 转换并返回分页结果
        return convertToPracticePageResultList(userId,demandIPage);
    }


    /**
     * 根据ID查询需求信息
     */
    @Override
    public Demand getDemand(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandMapper.selectById(demandId);

        // 2. 校验需求是否存在
        if(demand == null) {
            throw new BusinessException( ResultCode.DEMAND_NOT_EXIST);
        }
        return demand;
    }


    /**
     * 获取公开需求详情
     */
    @Override
    public DemandPublicDetailVO getPublicDemandDetail(Long demandId) {
        // 1. 查询需求基础信息
        Demand demand = getDemand(demandId);

        // 2. 校验需求是否已发布
        if (Objects.equals(demand.getStatus(), DemandStatusEnum.PUBLISHED.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_PUBLISHED);
        }

        // 3. 转换为公开详情VO
        DemandPublicDetailVO demandPublicDetailVO = new DemandPublicDetailVO();
        BeanUtils.copyProperties(demand, demandPublicDetailVO);
        BeanUtils.copyProperties(sysUserService.getUserProfile(demand.getPublisherId()), demandPublicDetailVO.getDemandPublisherDetailVO());

        return demandPublicDetailVO;
    }


    /**
     * 转换为实践列表分页结果
     */
    @Override
    public PageResult<DemandListVO> convertToPracticePageResultList(Long userId, IPage<Demand> demandIPage) {
        // 1. 转换需求列表为VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {
                    // 公共转换
                    DemandListVO vo = convertToDemandListVO(demand);

                    // 2. 设置实践成员信息
                    LambdaQueryWrapper<DemandMember> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(DemandMember::getDemandId, demand.getId())
                            .eq(DemandMember::getUserId, userId);
                    DemandMember member = demandMemberService.getOne(queryWrapper);

                    DemandPracticeList practiceList = new DemandPracticeList();
                    if (member != null) {
                        BeanUtils.copyProperties(member, practiceList);
                    }
                    vo.setDemandPracticeList(practiceList);

                    return vo;
                })
                .toList();

        // 3. 封装并返回结果
        return buildPageResult(demandIPage, voList);
    }


    /**
     * 转换为申请列表分页结果
     */
    @Override
    public PageResult<DemandListVO> convertToApplyPageResultList(IPage<Demand> demandIPage, Map<Long, DemandApplyList> applyStatusMap) {
        // 1. 转换需求列表为VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {
                    // 公共转换
                    DemandListVO vo = convertToDemandListVO(demand);

                    // 2. 设置申请状态
                    DemandApplyList apply = applyStatusMap.get(demand.getId());
                    vo.setDemandApplyList(apply);

                    return vo;
                })
                .toList();

        // 3. 封装并返回结果
        return buildPageResult(demandIPage, voList);
    }


    /**
     * 转换为我的需求列表分页结果
     */
    @Override
    public PageResult<DemandListVO> convertToMyPageResultList(IPage<Demand> demandIPage) {
        // 1. 转换需求列表为VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(this::convertToDemandListVO)
                .toList();

        // 2. 封装并返回结果
        return buildPageResult(demandIPage, voList);
    }


    /**
     * 转换为管理员需求列表分页结果
     */
    @Override
    public PageResult<DemandListVO> convertToAdminPageResultList(IPage<Demand> demandIPage){
        // 1. 转换需求列表为VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(this::convertToDemandListVO)
                .toList();

        // 2. 封装并返回结果
        return buildPageResult(demandIPage, voList);
    }


    /**
     * 公共核心转换：Demand → DemandListVO（基础字段 + 发布人）
     */
    private DemandListVO convertToDemandListVO(Demand demand) {
        // 1. 拷贝基础字段
        DemandListVO vo = new DemandListVO();
        BeanUtils.copyProperties(demand, vo);

        // 2. 拷贝发布人信息
        UserProfileVO userBase = sysUserService.getUserProfile(demand.getPublisherId());
        DemandPublisherList publisherList = new DemandPublisherList();
        BeanUtils.copyProperties(userBase, publisherList);
        publisherList.setAvatarUrl(minioService.getUserAvatarUrl(userBase.getAvatar()));
        vo.setDemandPublisherList(publisherList);

        return vo;
    }


    /**
     * 公共分页结果封装
     */
    private PageResult<DemandListVO> buildPageResult(IPage<Demand> page, List<DemandListVO> voList) {
        // 1. 封装分页信息
        PageResult<DemandListVO> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(voList);
        return result;
    }
}
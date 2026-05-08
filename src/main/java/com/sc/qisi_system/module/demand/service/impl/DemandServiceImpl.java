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

@RequiredArgsConstructor
@Service
public class DemandServiceImpl extends ServiceImpl<DemandMapper, Demand> implements DemandService {


    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final ApplyService applyService;
    private final DemandMemberService demandMemberService;
    private final MinioService minioService;


    @Override
    public boolean isNotExistsByDemandId(Long demandId) {
        return getById(demandId) != null;
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

        return convertToApplyPageResultList(demandIPage,applyService.getUserApplyMap(userId));
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
    public PageResult<DemandListVO> getMyJoinedPracticeList(Long userId, PracticeDemandQueryDTO queryDTO) {

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


    @Override
    public Demand getDemand(Long demandId) {

        Demand demand = demandMapper.selectById(demandId);

        if(demand == null) {
            throw new BusinessException( ResultCode.DEMAND_NOT_EXIST);
        }
        return demand;
    }



    @Override
    public DemandPublicDetailVO getPublicDemandDetail(Long demandId) {

        Demand demand = getDemand(demandId);

        if (Objects.equals(demand.getStatus(), DemandStatusEnum.PUBLISHED.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_NOT_PUBLISHED);
        }

        DemandPublicDetailVO demandPublicDetailVO = new DemandPublicDetailVO();
        BeanUtils.copyProperties(demand, demandPublicDetailVO);
        BeanUtils.copyProperties(sysUserService.getUserProfile(demand.getPublisherId()), demandPublicDetailVO.getDemandPublisherDetailVO());

        return demandPublicDetailVO;
    }



    @Override
    public PageResult<DemandListVO> convertToPracticePageResultList(Long userId, IPage<Demand> demandIPage) {
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {
                    // 公共转换
                    DemandListVO vo = convertToDemandListVO(demand);

                    // 【独有逻辑】设置实践成员信息
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

        return buildPageResult(demandIPage, voList);
    }


    @Override
    public PageResult<DemandListVO> convertToApplyPageResultList(IPage<Demand> demandIPage, Map<Long, DemandApplyList> applyStatusMap) {
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(demand -> {
                    // 公共转换
                    DemandListVO vo = convertToDemandListVO(demand);

                    // 【独有逻辑】设置申请状态
                    DemandApplyList apply = applyStatusMap.get(demand.getId());
                    vo.setDemandApplyList(apply);

                    return vo;
                })
                .toList();

        return buildPageResult(demandIPage, voList);
    }


    @Override
    public PageResult<DemandListVO> convertToMyPageResultList(IPage<Demand> demandIPage) {
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(this::convertToDemandListVO) // 直接调用公共方法
                .toList();

        return buildPageResult(demandIPage, voList);
    }


    @Override
    public PageResult<DemandListVO> convertToAdminPageResultList(IPage<Demand> demandIPage){
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(this::convertToDemandListVO)
                .toList();
        return buildPageResult(demandIPage, voList);
    }


    /**
     * 公共核心转换：Demand → DemandListVO（基础字段 + 发布人）
     */
    private DemandListVO convertToDemandListVO(Demand demand) {
        DemandListVO vo = new DemandListVO();
        // 拷贝基础字段
        BeanUtils.copyProperties(demand, vo);

        // 拷贝发布人信息
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
        PageResult<DemandListVO> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(voList);
        return result;
    }
}


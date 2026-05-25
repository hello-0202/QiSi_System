package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.common.enums.*;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.admin.dto.DateQueryDTO;
import com.sc.qisi_system.module.admin.vo.AdminStatVO;
import com.sc.qisi_system.module.admin.vo.AdminWorkbenchStatVO;
import com.sc.qisi_system.module.admin.vo.DemandCategoryVO;
import com.sc.qisi_system.module.admin.vo.DemandDailyTrendVO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.domain.DemandPracticeList;
import com.sc.qisi_system.module.demand.domain.DemandPublisherList;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.demand.vo.DemandPublisherDetailVO;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.user.vo.UserWorkbenchStatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 获取我发布的需求列表
     */
    @Override
    public PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO) {
        // 1. 构建分页和查询条件
        Page<Demand> page = new Page<>(myDemandQueryDTO.getPageNum(), myDemandQueryDTO.getPageSize());
        LambdaQueryWrapper<Demand> queryWrapper = new LambdaQueryWrapper<>();

        // 2. 设置查询条件
        queryWrapper
                .eq(Demand::getPublisherId, userId)
                .ge(Demand::getStatus, DemandStatusEnum.DRAFT.getCode())
                .ge(myDemandQueryDTO.getCreateTime() != null, Demand::getCreateTime, myDemandQueryDTO.getCreateTime())
                .in(CollectionUtils.isNotEmpty(myDemandQueryDTO.getStatusList()), Demand::getStatus, myDemandQueryDTO.getStatusList())
                .orderByDesc(Demand::getCreateTime)
                .orderByDesc(Demand::getProgressPercent);

        // 3. 分页查询并转换结果
        IPage<Demand> demandIPage = demandMapper.selectPage(page, queryWrapper);
        return convertToMyPageResultList(demandIPage);
    }


    /**
     * 根据ID查询需求信息
     */
    @Override
    public Demand getDemand(Long demandId) {
        // 1. 查询需求信息
        Demand demand = demandMapper.selectById(demandId);

        // 2. 校验需求是否存在
        if (demand == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
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
        if (Objects.equals(demand.getStatus(), DemandStatusEnum.DRAFT.getCode())) {
            throw new BusinessException(ResultCode.DEMAND_STATUS_ERROR);
        }

        // 3. 转换为公开详情VO
        DemandPublicDetailVO demandPublicDetailVO = new DemandPublicDetailVO();
        BeanUtils.copyProperties(demand, demandPublicDetailVO);
        demandPublicDetailVO.setCategory(DemandCategoryEnum.getDescByCode(demand.getCategory()));

        DemandPublisherDetailVO publisherDetailVO = new DemandPublisherDetailVO();
        BeanUtils.copyProperties(sysUserService.getUserProfile(demand.getPublisherId()), publisherDetailVO);
        demandPublicDetailVO.setDemandPublisherDetailVO(publisherDetailVO);

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
                    queryWrapper
                            .eq(DemandMember::getDemandId, demand.getId())
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
    public PageResult<DemandListVO> convertToAdminPageResultList(IPage<Demand> demandIPage) {
        // 1. 转换需求列表为VO
        List<DemandListVO> voList = demandIPage.getRecords().stream()
                .map(this::convertToDemandListVO)
                .toList();

        // 2. 封装并返回结果
        return buildPageResult(demandIPage, voList);
    }


    /**
     * 获取管理员工作台统计数据
     */
    @Override
    public AdminWorkbenchStatVO getWorkbenchStatistics() {
        AdminWorkbenchStatVO vo = new AdminWorkbenchStatVO();
        List<AdminWorkbenchStatVO.StatCard> statList = new ArrayList<>();

        // 需求总数
        int nowTotal = (int) demandMapper.countAdminTotalDemand();
        int yesTotal = (int) demandMapper.countAdminTotalDemandYesterday();
        AdminWorkbenchStatVO.StatCard totalCard = new AdminWorkbenchStatVO.StatCard();
        totalCard.setKey(AdminWorkbenchStatEnum.TOTAL_DEMANDS.getKey());
        totalCard.setLabel(AdminWorkbenchStatEnum.TOTAL_DEMANDS.getLabel());
        totalCard.setValue(nowTotal);
        totalCard.setTrend(nowTotal - yesTotal);
        statList.add(totalCard);

        // 研究中项目
        int nowOngoing = (int) demandMapper.countAdminOngoingDemand();
        int yesOngoing = (int) demandMapper.countAdminOngoingDemandYesterday();
        AdminWorkbenchStatVO.StatCard ongoingCard = new AdminWorkbenchStatVO.StatCard();
        ongoingCard.setKey(AdminWorkbenchStatEnum.ONGOING_PROJECTS.getKey());
        ongoingCard.setLabel(AdminWorkbenchStatEnum.ONGOING_PROJECTS.getLabel());
        ongoingCard.setValue(nowOngoing);
        ongoingCard.setTrend(nowOngoing - yesOngoing);
        statList.add(ongoingCard);

        // 已完成项目
        int nowComplete = (int) demandMapper.countAdminCompletedDemand();
        int yesComplete = (int) demandMapper.countAdminCompletedDemandYesterday();
        AdminWorkbenchStatVO.StatCard completeCard = new AdminWorkbenchStatVO.StatCard();
        completeCard.setKey(AdminWorkbenchStatEnum.COMPLETED_PROJECTS.getKey());
        completeCard.setLabel(AdminWorkbenchStatEnum.COMPLETED_PROJECTS.getLabel());
        completeCard.setValue(nowComplete);
        completeCard.setTrend(nowComplete - yesComplete);
        statList.add(completeCard);

        // 待审核
        int nowPending = (int) demandMapper.countAdminPendingReviewDemand();
        int yesPending = (int) demandMapper.countAdminPendingReviewDemandYesterday();
        AdminWorkbenchStatVO.StatCard pendingCard = new AdminWorkbenchStatVO.StatCard();
        pendingCard.setKey(AdminWorkbenchStatEnum.PENDING_REVIEW.getKey());
        pendingCard.setLabel(AdminWorkbenchStatEnum.PENDING_REVIEW.getLabel());
        pendingCard.setValue(nowPending);
        pendingCard.setTrend(nowPending - yesPending);
        statList.add(pendingCard);

        vo.setStatList(statList);
        return vo;
    }


    /**
     * 用户工作台统计（我的申请 + 进行中 + 已完成 + 待处理通知）
     */
    @Override
    public UserWorkbenchStatVO getUserWorkbenchStatistics() {
        // 1. 获取当前登录用户ID
        Long userId = SecurityUtils.getCurrentUserId();

        UserWorkbenchStatVO vo = new UserWorkbenchStatVO();
        List<UserWorkbenchStatVO.StatCard> statList = new ArrayList<>();

        // ===================== 1. 我的申请数量（我发布的需求） =====================
        UserWorkbenchStatVO.StatCard myApply = new UserWorkbenchStatVO.StatCard();
        myApply.setKey(UserWorkbenchStatEnum.MY_APPLY.getKey());
        myApply.setLabel(UserWorkbenchStatEnum.MY_APPLY.getLabel());
        long  myApplyNow = demandMapper.countUserMyApply(userId);
        long  myApplyYes = demandMapper.countUserMyApplyYesterday(userId);
        myApply.setValue((int) myApplyNow);
        myApply.setTrend((int) (myApplyNow - myApplyYes));
        statList.add(myApply);

        // ===================== 2. 进行中数量（我是成员，demand.status=4） =====================
        UserWorkbenchStatVO.StatCard ongoing = new UserWorkbenchStatVO.StatCard();
        ongoing.setKey(UserWorkbenchStatEnum.ONGOING.getKey());
        ongoing.setLabel(UserWorkbenchStatEnum.ONGOING.getLabel());
        long ongoingNow = demandMapper.countUserOngoing(userId);
        long ongoingYes = demandMapper.countUserOngoingYesterday(userId);
        ongoing.setValue((int) ongoingNow);
        ongoing.setTrend((int) (ongoingNow - ongoingYes));
        statList.add(ongoing);

        // ===================== 3. 已完成数量（我是成员，demand.status=5） =====================
        UserWorkbenchStatVO.StatCard completed = new UserWorkbenchStatVO.StatCard();
        completed.setKey(UserWorkbenchStatEnum.COMPLETED.getKey());
        completed.setLabel(UserWorkbenchStatEnum.COMPLETED.getLabel());
        long completedNow = demandMapper.countUserCompleted(userId);
        long completedYes = demandMapper.countUserCompletedYesterday(userId);
        completed.setValue((int) completedNow);
        completed.setTrend((int) (completedNow - completedYes));
        statList.add(completed);

        // ===================== 4. 待处理通知 =====================
        UserWorkbenchStatVO.StatCard pendingNotice = new UserWorkbenchStatVO.StatCard();
        pendingNotice.setKey(UserWorkbenchStatEnum.PENDING_NOTICE.getKey());
        pendingNotice.setLabel(UserWorkbenchStatEnum.PENDING_NOTICE.getLabel());
        pendingNotice.setValue(null);
        pendingNotice.setTrend(0);
        statList.add(pendingNotice);

        // 封装返回
        vo.setStatList(statList);
        return vo;
    }


    /**
     * 获取管理员数据统计界面数据
     */
    @Override
    public AdminStatVO getAdminDataStatistics() {
        AdminStatVO vo = new AdminStatVO();
        List<AdminStatVO.StatCard> list = new ArrayList<>();

        // 1 总需求数
        AdminStatVO.StatCard card1 = new AdminStatVO.StatCard();
        card1.setKey(AdminStatEnum.TOTAL_DEMAND.getKey());
        card1.setLabel(AdminStatEnum.TOTAL_DEMAND.getLabel());
        long totalDemand = demandMapper.countStatTotalDemand();
        card1.setValue(Math.toIntExact(totalDemand));
        list.add(card1);

        // 2 待审核数量 status=1
        AdminStatVO.StatCard card2 = new AdminStatVO.StatCard();
        card2.setKey(AdminStatEnum.PENDING_REVIEW.getKey());
        card2.setLabel(AdminStatEnum.PENDING_REVIEW.getLabel());
        long pendingReview = demandMapper.countStatPendingReview();
        card2.setValue(Math.toIntExact(pendingReview));
        list.add(card2);

        // 3 已发布数量 status=3
        AdminStatVO.StatCard card3 = new AdminStatVO.StatCard();
        card3.setKey(AdminStatEnum.PUBLISHED.getKey());
        card3.setLabel(AdminStatEnum.PUBLISHED.getLabel());
        long published = demandMapper.countStatPublished();
        card3.setValue(Math.toIntExact(published));
        list.add(card3);

        // 4 进行中数量 status=4
        AdminStatVO.StatCard card4 = new AdminStatVO.StatCard();
        card4.setKey(AdminStatEnum.ONGOING.getKey());
        card4.setLabel(AdminStatEnum.ONGOING.getLabel());
        long ongoing = demandMapper.countStatOngoing();
        card4.setValue(Math.toIntExact(ongoing));
        list.add(card4);

        // 5 已完成数量 status=5
        AdminStatVO.StatCard card5 = new AdminStatVO.StatCard();
        card5.setKey(AdminStatEnum.COMPLETED.getKey());
        card5.setLabel(AdminStatEnum.COMPLETED.getLabel());
        long completed = demandMapper.countStatCompleted();
        card5.setValue(Math.toIntExact(completed));
        list.add(card5);

        // 6 已注册用户数
        AdminStatVO.StatCard card6 = new AdminStatVO.StatCard();
        card6.setKey(AdminStatEnum.TOTAL_USER.getKey());
        card6.setLabel(AdminStatEnum.TOTAL_USER.getLabel());
        long totalUser = sysUserService.countTotalUser();
        card6.setValue(Math.toIntExact(totalUser));
        list.add(card6);

        // 7 总申请数量（demand_member 总记录）
        AdminStatVO.StatCard card7 = new AdminStatVO.StatCard();
        card7.setKey(AdminStatEnum.TOTAL_APPLY.getKey());
        card7.setLabel(AdminStatEnum.TOTAL_APPLY.getLabel());
        long totalApply = demandMemberService.countTotalApply();
        card7.setValue(Math.toIntExact(totalApply));
        list.add(card7);

        // 8 项目完成率
        AdminStatVO.StatCard card8 = new AdminStatVO.StatCard();
        card8.setKey(AdminStatEnum.FINISH_RATE.getKey());
        card8.setLabel(AdminStatEnum.FINISH_RATE.getLabel());
        int rate = 0;
        if (totalDemand > 0) {
            rate = (int) (completed * 100 / totalDemand);
        }
        card8.setValue(rate);
        list.add(card8);

        vo.setStatList(list);
        return vo;
    }


    /**
     * 根据日期范围查询需求每日趋势统计
     */
    @Override
    public List<DemandDailyTrendVO> selectDemandTrendByDate(DateQueryDTO dateQueryDTO) {
        return demandMapper.selectDemandTrendByDate(dateQueryDTO);
    }


    /**
     * 获取需求分类分布统计数据
     */
    @Override
    public List<DemandCategoryVO> getDemandCategoryDistribution() {
        return demandMapper.countDemandByCategory();
    }


    /**
     * 获取最新发布的需求列表
     */
    @Override
    public List<DemandListVO> getLatestDemandList() {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getStatus, DemandStatusEnum.PUBLISHED.getCode());
        wrapper.orderByDesc(Demand::getCreateTime);
        wrapper.last("LIMIT 10");
        return demandMapper.selectList(wrapper).stream()
                .map(this::convertToVO)
                .toList();
    }


    private DemandListVO convertToVO(Demand demand) {
        DemandListVO vo = new DemandListVO();
        vo.setId(demand.getId());
        vo.setTitle(demand.getTitle());
        vo.setCategory(demand.getCategory());
        vo.setStatus(demand.getStatus());
        vo.setCreateTime(demand.getCreateTime());
        return vo;
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
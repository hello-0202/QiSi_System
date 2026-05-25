package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.admin.vo.StatusDataVO;


public interface StatisticsService {


    /**
     * 查询已发布需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    String getPublishedDemandCount();


    /**
     * 查询研究中需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    String getResearchingDemandCount();





    /**
     * 查询需求状态分布统计接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    StatusDataVO getDemandStatusDistribution();


}

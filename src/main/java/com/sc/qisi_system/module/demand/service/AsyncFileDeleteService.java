package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.practice.entity.DemandProgressAttachment;


/**
 * 异步文件删除服务接口
 * 功能: 异步删除需求附件、需求进度附件等文件资源，避免阻塞主线程
 */
public interface AsyncFileDeleteService {


    /**
     * 异步删除需求附件
     *
     * @param attachment 需求附件实体
     */
    void deleteFileAsync(DemandAttachment attachment);


    /**
     * 异步删除需求进度附件
     *
     * @param attachment 需求进度附件实体
     */
    void deleteProgressFileAsync(DemandProgressAttachment attachment);
}
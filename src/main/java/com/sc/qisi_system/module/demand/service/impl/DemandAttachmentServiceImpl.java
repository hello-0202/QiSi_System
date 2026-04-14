package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.demand.mapper.DemandAttachmentMapper;
import org.springframework.stereotype.Service;

// 核心：继承 ServiceImpl（MP已实现所有IService方法），而非直接implements IService
@Service
public class DemandAttachmentServiceImpl extends ServiceImpl<DemandAttachmentMapper, DemandAttachment>
        implements IService<DemandAttachment> {




}
package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.demand.mapper.DemandAttachmentMapper;
import com.sc.qisi_system.module.demand.service.DemandAttachmentService;
import org.springframework.stereotype.Service;

@Service
public class DemandAttachmentServiceImpl extends ServiceImpl<DemandAttachmentMapper, DemandAttachment>
        implements DemandAttachmentService {
}
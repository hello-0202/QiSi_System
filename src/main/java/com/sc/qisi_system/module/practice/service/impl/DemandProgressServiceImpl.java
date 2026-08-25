package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.practice.entity.DemandProgress;
import com.sc.qisi_system.module.practice.mapper.DemandProgressMapper;
import com.sc.qisi_system.module.practice.service.DemandProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DemandProgressServiceImpl extends ServiceImpl<DemandProgressMapper, DemandProgress> implements DemandProgressService {
}

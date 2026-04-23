package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.apply.mapper.DemandMemberMapper;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import org.springframework.stereotype.Service;

@Service
public class DemandMemberServiceImpl extends ServiceImpl<DemandMemberMapper, DemandMember> implements DemandMemberService {
}

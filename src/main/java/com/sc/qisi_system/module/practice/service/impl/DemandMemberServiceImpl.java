package com.sc.qisi_system.module.practice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.practice.mapper.DemandMemberMapper;
import com.sc.qisi_system.module.practice.entity.DemandMember;
import com.sc.qisi_system.module.practice.service.DemandMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DemandMemberServiceImpl extends ServiceImpl<DemandMemberMapper, DemandMember> implements DemandMemberService {


    private final DemandMemberMapper demandMemberMapper;


    @Override
    public Long countTotalApply() {
        return demandMemberMapper.countTotalApply();
    }
}

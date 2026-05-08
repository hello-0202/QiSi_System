package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.user.entity.SchoolStaff;
import com.sc.qisi_system.module.user.mapper.SchoolStaffMapper;
import com.sc.qisi_system.module.user.service.SchoolStaffService;
import org.springframework.stereotype.Service;

@Service
public class SchoolStaffServiceImpl extends ServiceImpl<SchoolStaffMapper, SchoolStaff> implements SchoolStaffService {
}

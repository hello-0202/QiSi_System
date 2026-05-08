package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.user.entity.SchoolStudent;
import com.sc.qisi_system.module.user.mapper.SchoolStudentMapper;
import com.sc.qisi_system.module.user.service.SchoolStudentService;
import org.springframework.stereotype.Service;

@Service
public class SchoolStudentImpl extends ServiceImpl<SchoolStudentMapper, SchoolStudent> implements SchoolStudentService {
}

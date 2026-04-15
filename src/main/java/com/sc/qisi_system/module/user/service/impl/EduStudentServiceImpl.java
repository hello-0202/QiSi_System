package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.user.entity.EduStudent;
import com.sc.qisi_system.module.user.mapper.EduStudentMapper;
import com.sc.qisi_system.module.user.service.EduStudentService;
import org.springframework.stereotype.Service;

@Service
public class EduStudentServiceImpl extends ServiceImpl<EduStudentMapper, EduStudent> implements EduStudentService {
}

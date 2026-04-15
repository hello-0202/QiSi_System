package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.user.entity.EntEmployee;
import com.sc.qisi_system.module.user.mapper.EntEmployeeMapper;
import com.sc.qisi_system.module.user.service.EntEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EntEmployeeServiceImpl extends ServiceImpl<EntEmployeeMapper, EntEmployee> implements EntEmployeeService {
}

package com.sc.qisi_system.module.demand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.DemandService;
import org.springframework.stereotype.Service;

@Service
public class DemandServiceImpl extends ServiceImpl<DemandMapper, Demand> implements DemandService {


    @Override
    public boolean notExistsByDemandId(Long demandId) {
        return getById(demandId) != null;
    }
}

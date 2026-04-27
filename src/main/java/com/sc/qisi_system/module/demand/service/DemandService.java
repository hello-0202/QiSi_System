package com.sc.qisi_system.module.demand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.demand.entity.Demand;

public interface DemandService extends IService<Demand> {


    boolean notExistsByDemandId(Long demandId);

}

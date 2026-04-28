package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.practice.entity.DemandProgressAttachment;

public interface AsyncFileDeleteService {


    void deleteFileAsync(DemandAttachment attachment);


    void deleteProgressFileAsync(DemandProgressAttachment attachment);

}

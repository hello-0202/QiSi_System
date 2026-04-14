package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.module.demand.entity.DemandAttachment;

public interface AsyncFileDeleteService {

    void deleteFileAsync(DemandAttachment attachment);

}

package com.sc.qisi_system.module.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class StatusDataVO {

    /**
    * 需求
     */
    private List<String> statusList;

    /**
    * 接口数量
     */
    private List<Long> numberList;

}

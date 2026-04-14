package com.sc.qisi_system.common.result;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {


    /**
     * 总记录数
     */
    private long total;      // 总记录数


    /**
     * 总页数
     */
    private long pages;      // 总页数


    /**
     * 数据列表
     */
    private List<T> records; // 数据列表

}

package com.sc.qisi_system.common.result;

import lombok.Data;

import java.util.Collections;
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


    // 新增这个静态方法
    public static <T> PageResult<T> empty() {
        PageResult<T> result = new PageResult<>();
        result.setTotal(0L);
        result.setPages(0L);
        result.setRecords(Collections.emptyList());
        return result;
    }
}

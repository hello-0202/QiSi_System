package com.sc.qisi_system.module.admin.vo;

import lombok.Data;
import java.util.List;


@Data
public class AdminWorkbenchStatVO {


    private List<StatCard> statList;


    @Data
    public static class StatCard {
        /** 唯一标识：需求总数/进行中/已完成/待审核 */
        private String key;
        /** 显示名称 */
        private String label;
        /** 数量 */
        private Integer value;
        /** 趋势（同比/环比） */
        private Integer trend;
    }
}

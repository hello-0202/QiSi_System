package com.sc.qisi_system.module.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminStatVO {


    private List<StatCard> statList;


    @Data
    public static class StatCard {
        private String key;
        private String label;
        private Integer value;
    }
}
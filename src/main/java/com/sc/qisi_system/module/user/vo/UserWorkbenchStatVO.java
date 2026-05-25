package com.sc.qisi_system.module.user.vo;

import lombok.Data;
import java.util.List;


@Data
public class UserWorkbenchStatVO {


    private List<StatCard> statList;


    @Data
    public static class StatCard {
        private String key;
        private String label;
        private Integer value;
        private Integer trend;
    }
}
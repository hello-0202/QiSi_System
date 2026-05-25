package com.sc.qisi_system.module.admin.vo;

import lombok.Data;


@Data
public class DemandCategoryVO {


    // 分类名称（技术需求/研究需求/创新创意/其他）
    private String categoryName;


    // 该分类下的需求数量
    private Long count;
}
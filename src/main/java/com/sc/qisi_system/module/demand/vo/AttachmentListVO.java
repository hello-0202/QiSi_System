package com.sc.qisi_system.module.demand.vo;


import lombok.Data;

@Data
public class AttachmentListVO {


    /**
     * 主键ID
     */
    private Long id;


    /**
     * 文件名
     */
    private String fileName;


    /**
     * 文件大小
     */
    private Long fileSize;


    /**
     * 文件类型
     */
    private String fileType;


    /**
     * url
     */
    private String url;
}

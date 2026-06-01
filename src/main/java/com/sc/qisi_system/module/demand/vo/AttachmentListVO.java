package com.sc.qisi_system.module.demand.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class AttachmentListVO {


    /**
     * 主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
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

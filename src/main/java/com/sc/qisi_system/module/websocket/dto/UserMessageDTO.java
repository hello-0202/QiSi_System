package com.sc.qisi_system.module.websocket.dto;


import lombok.Data;

@Data
public class UserMessageDTO {


    /**
     * 所属对话ID
     */
    private String sessionId;


    /**
     * 发送者id
     */
    private String fromUserId;


    /**
     * 接收者id
     */
//    @JsonSerialize(using = ToStringSerializer.class)
    private String toUserId;


    /**
     * 消息内容
     */
    private String content;


    /**
     * 是否已读 0-未读 1-已读
     */
    private Integer status;
}

package com.sc.qisi_system.module.websocket.dto;


import lombok.Data;

@Data
public class UserMessageDTO {


    /**
     * 所属对话ID
     */
    private Long sessionId;


    /**
     * 发送者id
     */
    private Long fromUserId;


    /**
     * 接收者id
     */
    private Long toUserId;


    /**
     * 消息内容
     */
    private String content;


    /**
     * 是否已读 0-未读 1-已读
     */
    private Integer status;
}

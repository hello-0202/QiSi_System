package com.sc.qisi_system.module.websocket.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionVO {


    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionId;


    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;// 对方用户ID


    private String nickname;      // 对方昵称


    private String avatar;        // 对方头像


    private String lastMessage;   // 最后一条消息


    private LocalDateTime lastTime;  // 最后时间


    private Integer unreadCount;  // 未读数量
}
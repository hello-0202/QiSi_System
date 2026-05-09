package com.sc.qisi_system.module.websocket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("chat_message")
public class ChatMessage implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 所属对话ID
     */
    private Long sessionId;


    /**
     * 发送者ID
     */
    private Long fromUserId;


    /**
     * 消息内容
     */
    private String content;


    /**
     * 0未读 1已读
     */
    private Integer status;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
package com.sc.qisi_system.module.websocket.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sc.qisi_system.common.enums.SessionTypeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@TableName("chat_session")
public class ChatSession implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 1-单聊 2-群聊
     *
     * @see SessionTypeEnum
     */
    private Integer sessionType;


    /**
     * 用户A
     */
    private Long userA;


    /**
     * 用户B
     */
    private Long userB;


    /**
     * 最后一条消息
     */
    private String lastMessage;


    /**
     * 最后消息时间
     */
    private LocalDateTime lastTime;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
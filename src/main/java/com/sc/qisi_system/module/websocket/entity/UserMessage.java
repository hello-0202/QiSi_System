package com.sc.qisi_system.module.websocket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("user_message")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMessage implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


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
package com.sc.qisi_system.module.websocket.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sc.qisi_system.module.websocket.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {
}

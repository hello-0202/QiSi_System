package com.sc.qisi_system.module.websocket.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.websocket.entity.UserMessage;
import com.sc.qisi_system.module.websocket.mapper.UserMessageMapper;
import com.sc.qisi_system.module.websocket.service.UserMessageService;
import org.springframework.stereotype.Service;

@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {
}

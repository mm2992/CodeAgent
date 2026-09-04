package com.ma.aiagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ma.aiagent.domain.Conversation;
import com.ma.aiagent.mapper.ConversationMapper;
import com.ma.aiagent.service.ConversationService;


import jakarta.annotation.sql.DataSourceDefinition;
import org.springframework.stereotype.Service;


@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

}





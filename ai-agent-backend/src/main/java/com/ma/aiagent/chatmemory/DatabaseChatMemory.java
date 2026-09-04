package com.ma.aiagent.chatmemory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.aiagent.convert.MessageConvert;
import com.ma.aiagent.domain.Conversation;
import com.ma.aiagent.service.ConversationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DatabaseChatMemory implements ChatMemory {

    @Resource
    private ConversationService conversationService;
    @Override
    public void add(String conversationId, List<Message> messages) {
        final List<Conversation> conversations = messages.stream().map(message -> MessageConvert.toConversation(message, conversationId))
                .collect(Collectors.toList());
        final boolean b = conversationService.saveBatch(conversations);
        if (b){
            log.info("对话保存成功，共保存"+conversations.size()+"条记录");
        }else {
            log.info("对话保存失败");
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        QueryWrapper<Conversation> queryWrapper=new QueryWrapper();
        queryWrapper.eq("conversationId", conversationId)
                .orderByDesc("createTime")
                .last("limit " + lastN);
        final List<Conversation> conversationList = new ArrayList<>(conversationService.list(queryWrapper));
        Collections.reverse(conversationList);

        final List<Message> messagesList = conversationList.stream()
                .map(conversation -> MessageConvert.toMessages(conversation)).collect(Collectors.toList());
        return messagesList;


    }

    @Override
    public void clear(String conversationId) {
        final boolean b = conversationService.removeById(conversationId);
        if (b){
            log.info(conversationId+"编号的记录已删除");
        }else {
            log.info("删除失败");
        }
    }

}

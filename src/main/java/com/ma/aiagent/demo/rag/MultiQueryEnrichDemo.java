package com.ma.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiQueryEnrichDemo {


    private ChatClient.Builder chatClientBuilder;

    public MultiQueryEnrichDemo(ChatModel dashscopeChatModel) {
        this.chatClientBuilder=ChatClient.builder(dashscopeChatModel);
    }

    public List<Query> queryExpand(){
        MultiQueryExpander multiQueryExpander=MultiQueryExpander.builder()
                .numberOfQueries(3)
                .chatClientBuilder(chatClientBuilder)
                .build();
        final List<Query> queryExpand = multiQueryExpander.expand(new Query("河北科技大学怎么样，用三个词回答啊啊啊啊啊"));
        return queryExpand;

    }
}

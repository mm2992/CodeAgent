package com.ma.aiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoveAppKeyWordConfig {

    @Resource
    ChatModel dashscopeChatModel;
    public List<Document> addMetadata(List<Document> documents){
        final KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 3);
        final List<Document> enrichDocuments = keywordMetadataEnricher.apply(documents);
        return enrichDocuments;
    }
}

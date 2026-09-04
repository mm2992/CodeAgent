package com.ma.aiagent.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;
    @Resource
    LoveAppKeyWordConfig loveAppKeyWordConfig;

    @Primary
    @Bean(name = "loveAppVectorStore")
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel){
        SimpleVectorStore simpleVectorStore=SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        final List<Document> documents = loveAppDocumentLoader.loadDocument();
        //为documents补充元信息
        final List<Document> enrichDocuments = loveAppKeyWordConfig.addMetadata(documents);
        simpleVectorStore.add(enrichDocuments);
        return simpleVectorStore;
    }

}

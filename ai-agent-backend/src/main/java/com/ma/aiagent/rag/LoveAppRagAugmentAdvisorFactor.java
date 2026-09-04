package com.ma.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建一个Rag检索增强顾问
 */
public class LoveAppRagAugmentAdvisorFactor {

    public static Advisor createLoveAppRagAugmentAdvisor(VectorStore vectorStore,String status){

        DocumentRetriever documentRetriever= VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.5)
                .topK(3)
                .filterExpression(
                        new FilterExpressionBuilder()
                        .eq("status",status)
                        .build()
                )
                .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(LoveAppContextAugmentFactory.queryAugment())
                .build();
    }
}

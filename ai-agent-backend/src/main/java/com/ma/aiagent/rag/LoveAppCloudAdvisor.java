package com.ma.aiagent.rag;

import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 自定义基于阿里云百炼的RAG问答
 */
@Component
@Slf4j
public class LoveAppCloudAdvisor {
    @Value("${spring.ai.dashscope.api-key}")
    private String key;

    @Bean
    public Advisor loveAppRagCloudAdvisor(){
        final DashScopeApi dashScopeApi = new DashScopeApi(key);
        String indexName="恋爱大师";
        DocumentRetriever dashScopeDocumentRetriever=new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder().withIndexName(indexName).build());
        final RetrievalAugmentationAdvisor build = RetrievalAugmentationAdvisor.builder().documentRetriever(dashScopeDocumentRetriever).build();
        return build;
    }
}

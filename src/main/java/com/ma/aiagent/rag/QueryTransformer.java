package com.ma.aiagent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写器
 */
@Component
public class QueryTransformer {
     private RewriteQueryTransformer queryTransformer;

    public QueryTransformer(ChatModel dashscopeChatModel) {
        final ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        queryTransformer=RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    public Query rewriteQuery(Query query){
        final Query transformedQuery = queryTransformer.transform(query);
        return transformedQuery;

    }
}

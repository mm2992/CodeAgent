package com.ma.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class QueryTransformerTest {
    @Resource
    private QueryTransformer queryTransformer;
    @Test
    void rewriteQuery() {
        Query query=new Query("河北科技大学怎么样,宿舍怎么样，环境怎么样，在哪个城市");
        final Query rewrittenQuerys = queryTransformer.rewriteQuery(query);
        Assertions.assertNotNull(rewrittenQuerys);
    }
}
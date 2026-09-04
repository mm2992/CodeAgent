package com.ma.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MultiQueryEnrichDemoTest {
    @Resource
    MultiQueryEnrichDemo multiQueryEnrichDemo;
    @Test
    void queryExpand() {
        final List<Query> queries = multiQueryEnrichDemo.queryExpand();
        Assertions.assertNotNull(queries);
    }
}
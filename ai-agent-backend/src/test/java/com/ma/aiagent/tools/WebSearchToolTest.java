package com.ma.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebSearchToolTest {
    @Value("${search.api.key}")
    String api_key;
    @Test
    void searchWeb() {

        WebSearchTool webSearchTool=new WebSearchTool(api_key);
        final String result = webSearchTool.searchWeb("河北科技大学怎么样");
        Assertions.assertNotNull(result);
    }
}
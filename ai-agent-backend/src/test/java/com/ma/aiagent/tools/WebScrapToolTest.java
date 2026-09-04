package com.ma.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapToolTest {

    @Test
    void scrap() {
        String url="https://www.codefather.cn/";
        WebScrapTool webScrapTool=new WebScrapTool();
        final String scrap = webScrapTool.scrap(url);
        Assertions.assertNotNull(scrap);
    }
}
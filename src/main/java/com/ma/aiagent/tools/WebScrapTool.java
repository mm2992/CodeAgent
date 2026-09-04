package com.ma.aiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapTool {

    @Tool(description = "scrap the content from the webPage")
    public String scrap(@ToolParam(description = "URL of the webPage to scrap") String url){
        try {
            final Document document = Jsoup.connect(url).get();
            return document.outerHtml();
        } catch (Exception e) {
            return "Error Scrap"+e.getMessage();
        }
    }
}

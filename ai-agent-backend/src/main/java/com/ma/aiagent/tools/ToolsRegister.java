package com.ma.aiagent.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsRegister {
    @Value("${search.api.key}")
    public  String apikey;
    @Bean
    public ToolCallback[] registerTools(){
        final CommandExecutorTool commandExecutorTool = new CommandExecutorTool();
        final FileOperateTool fileOperateTool = new FileOperateTool();
        final ResourceDownload resourceDownload = new ResourceDownload();
        final WebSearchTool webSearchTool = new WebSearchTool(apikey);
        final WebScrapTool webScrapTool = new WebScrapTool();
        final PdfGeneratorTool pdfGeneratorTool = new PdfGeneratorTool();
        final TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                commandExecutorTool,
                fileOperateTool,
                resourceDownload,
                webScrapTool,
                webSearchTool,
                pdfGeneratorTool,
                terminateTool

        );

    }
}

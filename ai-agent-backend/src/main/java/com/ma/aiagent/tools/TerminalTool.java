package com.ma.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

public class TerminalTool {

    @Tool(description = " Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.\n" +
            "When you have finished all the tasks, call this tool to end the work.")
    public String doTerminate(){
        return "terminate task";
    }
}

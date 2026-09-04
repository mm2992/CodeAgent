package com.ma.aiagent.agent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class MyManus extends ToolCallAgent{

    public MyManus(ToolCallback[] availableTools, ChatModel dashscopeChatModel) {
        super(availableTools);
        String systemPrompt = "You are YuManus, an all-capable AI assistant, aimed at solving any task presented by the user.\n" +
                "You have various tools at your disposal that you can call upon to efficiently complete complex requests.";
        setSystemPrompt(systemPrompt);
        String nextStepPrompt = "Based on user needs, proactively select the most appropriate tool or combination of tools.For complex tasks, you can break down the problem and use different tools step by step to solve it.After using each tool, clearly explain the execution results and suggest the next steps.\n" +
                "If you want to stop the interaction at any point, use the terminate' tool/function call.";
        setNextStepPrompt(nextStepPrompt);
        ChatClient chatClient= ChatClient.builder(dashscopeChatModel)
                .build();

        this.setChatClient(chatClient);
    }
}

package com.ma.aiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.ma.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public abstract class ToolCallAgent extends ReActAgent{

    //可调用的工具列表
    private final ToolCallback[] availableTools;
    //AI需要调用的工具
    private ChatResponse toolCallResponse;
    //工具管理
    private ToolCallingManager toolCallingManager;
    //工具调用
    private ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder().withProxyToolCalls(true).build();
    }


    @Override
    public boolean think() {
        //构造指令
        if (StrUtil.isNotBlank(getNextStepPrompt())){
            //把指令加到messages
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessages().add(userMessage);
        }
        List<Message> messages = getMessages();
        //调用AI
        Prompt prompt =new Prompt(messages,chatOptions);
        ChatResponse chatResponse = getChatClient()
                .prompt(prompt)
                .tools(availableTools)
                .call()
                .chatResponse();
        this.toolCallResponse = chatResponse;
        //解析要调用的工具
        //解析得到助手消息
        AssistantMessage assistantMessage = toolCallResponse.getResult().getOutput();
        //得到AI要调用的工具
        List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
        String toolCallInfo = toolCalls.stream()
                .map(tool ->
                        String.format("名称：%s，参数：%s", tool.name(), tool.arguments()))
                .collect(Collectors.joining("\n"));
        log.info("assistant tool call info:{}",toolCallInfo);
        if (CollectionUtil.isEmpty(toolCalls)){
            //没有工具调用,记录消息
            messages.add(assistantMessage);
            return false;
        }else {
            //有工具调用不记录消息
            return true;
        }
    }

    @Override
    public String act() {
        if (toolCallResponse == null){
            return "no tool call";
        }
        Prompt prompt =new Prompt(getMessages(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallResponse);
        setMessages(toolExecutionResult.conversationHistory());
        ToolResponseMessage  toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        String toolCallResults = toolResponseMessage.getResponses().stream()
                .map(toolCallResponse -> String.format("名称：%s，参数：%s", toolCallResponse.name(), toolCallResponse.responseData()))
                .collect(Collectors.joining("\n"));

        if (toolResponseMessage.getResponses()
                .stream()
                .anyMatch(toolCallResponse -> toolCallResponse.name().equals("doTerminate"))){
            //调用了终止工具，将agent状态设置为终止
            setState(AgentState.TERMINATED);
        }
        log.info("tool call result:{}",toolCallResults);

        return toolCallResults;
    }
}

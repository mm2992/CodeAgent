package com.ma.aiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.ma.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
@Slf4j
public abstract class BaseAgent {

    private String systemPrompt;
    private String userPrompt;
    private String nextStepPrompt;
    private AgentState state =AgentState.IDLE;
    private int currentStep =0;
    private int maxStep =10;
    private ChatClient chatClient;
    //维护会话信息
    private List<Message> messages = new ArrayList<>();
    public String run(String userPrompt){
        if (state!= AgentState.IDLE){
            throw new RuntimeException("agent is not idle");
        }
        if (StrUtil.isBlank(userPrompt)){
            throw new RuntimeException("prompt is blank");
        }
        //保存对话信息

        Message userMessage = new UserMessage(userPrompt);
        messages.add(userMessage);
        //保存结果
        List<String> results = new ArrayList<>();
        for (int i = 0; i < maxStep && state != AgentState.TERMINATED; i++) {
            currentStep = i+1;
            String stepResult = step(userPrompt);
            log.info("step:{},result:{}",currentStep,stepResult);
            results.add(stepResult);
        }
        if (currentStep >= maxStep){
            state = AgentState.TERMINATED;
            results.add("Terminated: max step reached");
        }

        return String.join("\n",results);
    }


    /**
     * 异步调用返回流式数据
     * @param userPrompt
     * @return
     */
    public SseEmitter runWithStream(String userPrompt){
        SseEmitter sseEmitter =new SseEmitter(300000L);
        CompletableFuture.runAsync(()->{
            if (state!= AgentState.IDLE){
                sseEmitter.completeWithError(new RuntimeException("Agent is busy"));
                return;
            }
            if (StrUtil.isBlank(userPrompt)){
                sseEmitter.completeWithError(new RuntimeException("User prompt is blank"));
            }
            //保存对话信息
            Message userMessage = new UserMessage(userPrompt);
            messages.add(userMessage);
            //保存结果
            List<String> results = new ArrayList<>();
            try {
                for (int i = 0; i < maxStep && state != AgentState.TERMINATED; i++) {
                    currentStep = i+1;
                    String stepResult = step(userPrompt);
                    log.info("step:{},result:{}",currentStep,stepResult);
                    results.add(stepResult);
                    sseEmitter.send(stepResult);
                }
                if (currentStep >= maxStep){
                    state = AgentState.TERMINATED;
                    results.add("Terminated: max step reached");
                    sseEmitter.send("Terminated: max step reached");
                }
            }catch (Exception e){
                sseEmitter.completeWithError(e);
            }

        });

        sseEmitter.onTimeout(()->{
            this.state = AgentState.ERROR;
        });

        sseEmitter.onCompletion(()-> {
            this.state = AgentState.TERMINATED;
        });
        return sseEmitter;
    }

    public abstract String step(String userPrompt);
}

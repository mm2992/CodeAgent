package com.ma.aiagent.controller;

import com.ma.aiagent.agent.MyManus;
import com.ma.aiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    LoveApp loveApp;

    @Resource
    ToolCallback[] allTools;
    @Resource
    ChatModel dashscopeChatModel;

    @GetMapping("/chat/sync")
    public String chatWithAppSync(String message,String chatId){
        return loveApp.doChat(message,chatId);
    }

    /**
     * 第一种方式将服务器的及时响应数据推送给前端
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithAppStream(String message, String chatId){
        return loveApp.doChatWithStream(message,chatId);
    }

    /**
     * 第二种方式将服务器的及时响应数据推送给前端
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/chat/serverEvent")
    public Flux<ServerSentEvent<String>> chatWithAppServerEvent(String message, String chatId){
        return loveApp.doChatWithStream(message,chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * 第二种方式将服务器的及时响应数据推送给前端
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/chat/sse")
    public SseEmitter chatWithAppSse(String message, String chatId){
        SseEmitter sseEmitter = new SseEmitter();
        loveApp.doChatWithStream(message,chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                },sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    @GetMapping("/manus/chat")
    public SseEmitter chatWithManusAsync(String message){
        MyManus myManus =new MyManus(allTools,dashscopeChatModel);
        SseEmitter sseEmitter = myManus.runWithStream(message);
        return sseEmitter;
    }
}

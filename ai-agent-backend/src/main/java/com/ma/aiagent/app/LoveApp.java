package com.ma.aiagent.app;

import com.ma.aiagent.advisor.MyLoggerAdvisor;
import com.ma.aiagent.chatmemory.DatabaseChatMemory;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;

import org.springframework.ai.chat.client.advisor.api.Advisor;


import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;


@Component
@Slf4j
@Data
public class LoveApp {
    //初始化对话客户端
    private final ChatClient chatClient;
    private static final String SYSTEM_CONTEXT="你是一名软件开发领域的专家,帮助解决软件开发中遇到的问题，你可以询问用户的在职情况，是否在职，是新手还是在职工程师还是项目负责人等,根据用户的信息来解决他们在工作中遇到的问题";

    public LoveApp(ChatModel dashscopeChatModel,DatabaseChatMemory databaseChatMemory){
        //自定义chatmemory
        String filePath=System.getProperty("user.dir")+"/tmp/chat_memory";
        //ChatMemory chatMemory=new FileBasedChatMemory(filePath);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_CONTEXT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(databaseChatMemory),
                        new MyLoggerAdvisor()
                ).build();

    }

    public String doChat(String message,String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",text);
        return text;
    }

    //临时定义一个类java14新特性
    record LoveReport(String title, List<String> suggestions){

    }
    public LoveReport doChatWithReport(String message,String chatId){
        LoveReport loveReport = chatClient.prompt()
                .system(SYSTEM_CONTEXT+"每次对话生成的结果的标题为{用户名}的问题解决方案，内容为建议列表，包含三条建议即可")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport:{}",loveReport);
        return loveReport;
    }

//基于RAG知识库的问答

    @Resource
    private VectorStore loveAppVectorStore;
    @Resource
    private VectorStore pgVectorStore;
    @Resource
    Advisor loveAppRagCloudAdvisor;
    public String doChatWithRag(String message,String chatId){
        final ChatResponse chatResponse = chatClient.prompt().user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                //基于本地知识库的检索
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                //基于云端知识库的Rag
                //.advisors(loveAppRagCloudAdvisor)
                //基于Rag的知识问答（PgVector）
                //.advisors(new QuestionAnswerAdvisor(pgVectorStore))
                //基于自定义Rag检索增强(知识查询+上下文增强)
//                .advisors(
//                        LoveAppRagAugmentAdvisorFactor.createLoveAppRagAugmentAdvisor(loveAppVectorStore,"新手")
//                )
                .call()
                .chatResponse();

        return chatResponse.getResult().getOutput().getText();
    }

    @Resource
    ToolCallback[] registerTools;
    public String doChatWithTool(String message,String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .tools(registerTools)
                .advisors(new MyLoggerAdvisor())
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",text);
        return text;
    }


    /**
     * 利用MCP服务
     */
    @Resource
    ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMcp(String message,String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .tools(toolCallbackProvider)
                .advisors(new MyLoggerAdvisor())
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",text);
        return text;
    }

    /**
     * 响应式返回
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatWithStream(String message, String chatId){
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }
}

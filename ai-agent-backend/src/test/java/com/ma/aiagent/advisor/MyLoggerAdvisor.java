package com.ma.aiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * 自定义日志拦截器
 */
@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    /**
     * 拦截器名称
     * @return
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 拦截器优先级，值越小优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 前置处理器
     * @param advisedRequest
     */
    private AdvisedRequest before(AdvisedRequest advisedRequest){
        String s = advisedRequest.userText();
        log.info("user:{}",s);
        return advisedRequest;
    }

    /**
     *
     * @param advisedResponse
     */
    private void after(AdvisedResponse advisedResponse){
        String AiResponse = advisedResponse.response().getResult().getOutput().getText();
        log.info("AI:{}",AiResponse);

    }
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        //调用前置处理器
        AdvisedRequest advisedRequest1 = this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest1);
        this.after(advisedResponse);
        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        AdvisedRequest advisedRequest1 = this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponseFlux = chain.nextAroundStream(advisedRequest1);

        //调用消息整合器将流式数据进行整合
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponseFlux,this::after);
    }


}

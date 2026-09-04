package com.ma.aiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 自定义上下文查询增强器
 */
public class LoveAppContextAugmentFactory {

    public static ContextualQueryAugmenter queryAugment(){
        PromptTemplate promptTemplate=new PromptTemplate(
                "你应该回答：我只能回答单身相关的问题，抱歉！"
        );
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(promptTemplate)
                .build();
    }
}

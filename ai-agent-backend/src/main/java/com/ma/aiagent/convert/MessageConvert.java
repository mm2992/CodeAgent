package com.ma.aiagent.convert;

import com.ma.aiagent.domain.Conversation;
import org.springframework.ai.chat.messages.*;

import java.util.List;

public class MessageConvert {
    /**
     * Message转换成对话
     * @param message
     * @param conversationId
     * @return
     */
    public static Conversation toConversation(Message message,String conversationId){
        final Conversation conversation = Conversation.builder()
                .conversationId(conversationId)
                .messageType(message.getMessageType().getValue())
                .content(message.getText())
                .build();
        return conversation;

    }

    /**
     * 对话转换成Message
     * @param conversation
     * @return
     */
    public static Message toMessages(Conversation conversation){
        String messageType=conversation.getMessageType();
        String content=conversation.getContent();
        return switch (messageType){
            case "user"->new UserMessage(content);
            case "assistant"->new AssistantMessage(content);
            case "system"->new SystemMessage(content);
            case "tool"->new ToolResponseMessage(List.of());
            default -> throw new IllegalStateException("Unexpected value: " + messageType);
        };
    }
}

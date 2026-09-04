package com.ma.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBasedChatMemory implements ChatMemory {
    private final String USER_DIR;
    static Kryo kryo=new Kryo();
    static {
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }
    public FileBasedChatMemory(String dir){
        this.USER_DIR=dir;
        File base_dir=new File(USER_DIR);
        if (!base_dir.exists()){
            base_dir.mkdirs();
        }
    }

    /**
     * 保存单条
     * @param conversationId
     * @param message
     */
    @Override
    public void add(String conversationId, Message message) {
        //获取现有的列表
        List<Message> conversationMessages = getConversationMessages(conversationId);
        conversationMessages.add(message);
        save(conversationId,conversationMessages);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        //获取现有的列表
        List<Message> conversationMessages = getConversationMessages(conversationId);
        conversationMessages.addAll(messages);
        save(conversationId,conversationMessages);
    }

    /**
     * 获取后几条
     * @param conversationId
     * @param lastN
     * @return
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> allMessages = getConversationMessages(conversationId);
        return allMessages.stream()
                .skip(Math.max(0, allMessages.size() - lastN))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        File file=new File(conversationId);
        if (file.exists()){
            file.delete();
        }
    }

    public void save(String conversationId,List<Message> messages){
        final File conversationFile = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(conversationFile))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 获取当前聊天的列表
     * @param conversationId
     * @return
     */
    public List<Message> getConversationMessages(String conversationId){
        File conversationFile = getConversationFile(conversationId);
        List<Message> messages=new ArrayList<>();
        if (!conversationFile.exists()||conversationFile.length()==0){
            return new ArrayList<>();
        }
        if (conversationFile.exists()){
            try {
                Input input=new Input(new FileInputStream(conversationFile));
                messages=kryo.readObject(input, ArrayList.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }
    /**
     * 根据conversationId取会话文件
     * @param conversationId
     * @return
     */
    public File getConversationFile(String conversationId){
        File conversationFile=new File(USER_DIR,conversationId+".kryo");
        if (!conversationFile.exists()){
            try {
                conversationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conversationFile;
    }
}

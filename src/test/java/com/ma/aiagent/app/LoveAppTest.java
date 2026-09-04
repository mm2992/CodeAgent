package com.ma.aiagent.app;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;
//    @Test
//    void doChat() {
//        String chatId= UUID.randomUUID().toString();
//        String message="";
//        //第一轮
//        message="你好，我叫李雷";
//        String s = loveApp.doChat(message, chatId);
//        Assertions.assertNotNull(s);
//        //第一轮
//        message="我的女朋友叫韩梅梅";
//        s = loveApp.doChat(message, chatId);
//        Assertions.assertNotNull(s);
//        //第一轮
//        message="我的女朋友叫什么？";
//        s = loveApp.doChat(message, chatId);
//        Assertions.assertNotNull(s);

    //}

    @Test
    void doChatWithReport() {
        String message="你好我叫刘梦，我的女朋友叫什么？";
        String chatId=UUID.randomUUID().toString();

        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String message="你好我是一名软件开发新手";
        String chatId=UUID.randomUUID().toString();
        final String s = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(s);
    }

    @Test
    void doChatWithTool() {
        //测试联网搜索
        //testMessage("请帮我推荐几个石家庄约会的去处");
        //测试资源下载
       // testMessage("我想要一张情侣壁纸，请帮我下载");
        //测试pdf生成
        testMessage("我想要一份情侣约会计划，把这句话保存为pdf");
        //测试文件操作
        //testMessage("河北科技大学是一个好地方，请把这句话保存到文件中");
        //测试命令执行
        //testMessage("执行python3脚本");
        //测试网页抓取
        //testMessage("最近想学编程了，去编程导航（codefather.cn）网站上看看给我一些建议，10个字就行");
    }

    public void testMessage(String message){
        String chatId=UUID.randomUUID().toString();
        final String s = loveApp.doChatWithTool(message, chatId);
        Assertions.assertNotNull(s);
    }

    @Test
    void doChatWithMCP() {
        String message="帮我搜索美丽的风景图片";
        String chatId=UUID.randomUUID().toString();
        final String s = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(s);
    }
}
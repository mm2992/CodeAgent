package com.ma.aiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyManusTest {

    @Resource
    private MyManus myManus;
    @Test
    void testMyManus() {
        String prompt = "你好，我现在石家庄，请帮我两个适合约会的地点。并结合一些网络图片制定一份详细的约会计划，以pdf格式进行输出";
        String run = myManus.run(prompt);
        Assertions.assertNotNull(run);
    }

}
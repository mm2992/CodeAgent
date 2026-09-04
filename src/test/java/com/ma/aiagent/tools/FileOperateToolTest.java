package com.ma.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileOperateToolTest {
    FileOperateTool fileOperateTool=new FileOperateTool();
    @Test
    void fileReader() {
        String filename="河北.txt";

        final String s = fileOperateTool.fileReader(filename);
        System.out.println(s);
        Assertions.assertNotNull(s);
    }

    @Test
    void fileWrite() {
        String filename="河北.txt";
        String content="这么近，那么美，周末到河北！";
        final String s = fileOperateTool.fileWrite(filename, content);
        Assertions.assertNotNull(s);
    }
}
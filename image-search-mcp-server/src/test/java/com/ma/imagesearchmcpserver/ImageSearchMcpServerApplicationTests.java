package com.ma.imagesearchmcpserver;

import com.ma.imagesearchmcpserver.tools.ImageSearchTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageSearchMcpServerApplicationTests {

    @Resource
    ImageSearchTool imageSearchTool;
    @Test
    void contextLoads() {
        String Images = imageSearchTool.searchImage("河北科技大学70周年校庆");
        Assertions.assertNotNull(Images);
    }

}

package com.ma.imagesearchmcpserver.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;


@Service
public class ImageSearchTool {
    private  final String API_KEY = "your-api-key"; // 替换为你的API密钥
    private  final String BASE_URL = "https://api.pexels.com/v1/search";
    @Tool
    public String searchImage(String query){
        try {
            // 发送GET请求
            HttpResponse response = HttpRequest.get(BASE_URL)
                    .header("Authorization", API_KEY)
                    .form("query", query)  // 添加query参数
                    .form("per_page", 15)  // 固定每页15条
                    .form("page", 1)       // 固定第1页
                    .timeout(5000)         // 设置5秒超时
                    .execute();

            // 检查响应状态
            if (response.isOk()) {
                return response.body().toString(); // 返回响应体的字符串
            } else {
                System.err.println("请求失败，状态码: " + response.getStatus());
                return "请求失败，状态码: " + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "请求异常: " + e.getMessage();
        }
    }
}

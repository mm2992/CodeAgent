package com.ma.aiagent.demo.invoke;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;


public class HttpAiInvoke {

    // 替换为你的DashScope API密钥
    private static final String DASHSCOPE_API_KEY = TestApi.Api_Key;
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    public static void main(String[] args) {
        try {
            // 1. 构建请求参数JSON
            JSONObject requestBody = buildRequestBody();

            // 2. 发送POST请求
            String response = sendPostRequest(requestBody);

            // 3. 处理响应结果
            handleResponse(response);

        } catch (Exception e) {
            System.err.println("调用DashScope API失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 构建请求体JSON对象
     */
    private static JSONObject buildRequestBody() {
        // 构建messages数组
        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "你是谁？");
        messages.add(userMessage);

        // 构建input对象
        JSONObject input = new JSONObject();
        input.put("messages", messages);

        // 构建parameters对象
        JSONObject parameters = new JSONObject();
        parameters.put("result_format", "message");

        // 构建完整请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-v3.1");
        requestBody.put("input", input);
        requestBody.put("parameters", parameters);

        return requestBody;
    }

    /**
     * 发送POST请求
     */
    private static String sendPostRequest(JSONObject requestBody) {
        // 设置请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + DASHSCOPE_API_KEY);
        headers.put("Content-Type", "application/json");

        // 发送POST请求并获取响应
        try (HttpResponse response = HttpRequest.post(API_URL)
                .addHeaders(headers)
                .body(requestBody.toString())
                .execute()) {

            // 检查响应状态码
            if (response.getStatus() != 200) {
                throw new RuntimeException("请求失败，状态码：" + response.getStatus()
                        + "，响应内容：" + response.body());
            }

            return response.body();
        }
    }

    /**
     * 处理API响应结果
     */
    private static void handleResponse(String responseBody) {
        if (JSONUtil.isJson(responseBody)) {
            JSONObject responseJson = JSONUtil.parseObj(responseBody);
            System.out.println("API响应结果（格式化）：");
            System.out.println(JSONUtil.toJsonPrettyStr(responseJson));

            // 可根据实际需求提取响应中的关键信息
            if (responseJson.containsKey("output")) {
                JSONObject output = responseJson.getJSONObject("output");
                JSONArray messages = output.getJSONArray("messages");
                if (messages != null && !messages.isEmpty()) {
                    String assistantReply = messages.getJSONObject(0).getStr("content");
                    System.out.println("\n助手回复：" + assistantReply);
                }
            }
        } else {
            System.out.println("API响应内容（非JSON）：" + responseBody);
        }
    }
}


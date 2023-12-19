package com.myworld.enen.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @ClassName GPTKit
 * @Author wkz
 * @Date 2023/12/5 11:17
 * @Version 1.0
 */
/*
* {
  "id": "chatcmpl-8SGS54wFRQYV0y3HH5hL1AV8F6x4L",
  "object": "chat.completion",
  "created": 1701747117,
  "model": "gpt-3.5-turbo-0613",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "你好！GPT模型的最大token数量取决于具体的模型架构和预训练过程。在GPT-3这样的较大模型中，最大token数量为2048个。\n但需要注意，进行预测或生成回答时，建议将输入文本限制在模型的最大输入限制范围内，以确保获得最佳的性能和效果。对于GPT-3模型而言，通常建议将输入限制在最大token数量的一半左右，即约1024个tokens。这样做可以避免模型的响应时间过长和性能下降。"
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 46,
    "completion_tokens": 174,
    "total_tokens": 220
  },
  "system_fingerprint": null
}
* */
public class GPTKit {
    public static String talkTochatGPT(String url, String apiKey, String model, Integer maxTokens, String question) throws IOException {
        System.out.println("url: " + url + "  apiKey: " + apiKey + "  model: " + model + "  maxTokens: " + maxTokens + "  question: " + question);
//        question = "hello！！！我们认识吗？";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        // The request body
        String body = "{\"model\": \"" + model + "\", \"max_tokens\":" + maxTokens + ", \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]}";
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();
        // Response from ChatGPT
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        System.out.println("response:  " + response);
        // calls the method to extract the message.
        return extractMessageFromJSONResponse(response.toString());
    }
    public static String extractMessageFromJSONResponse(String response) {
        JSONObject obj = JSON.parseObject(response);
        JSONArray choices = obj.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        return message.getString("content");

    }
}

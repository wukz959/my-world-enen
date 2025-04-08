package com.myworld.enen.Service;

import com.myworld.enen.utils.GPTKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName GPTService
 * @Author wkz
 * @Date 2023/12/5 14:53
 * @Version 1.0
 */
@Service
public class GPTService {
    
    @Value("${myGPT.url}")
    private String url;
    @Value("${myGPT.apiKey}")
    private String apiKey;
    @Value("${myGPT.model}")
    private String model;
    @Value("${myGPT.maxTokens}")
    private int maxTokens;
    
    public String askQuestion(String question) throws IOException {
        return GPTKit.talkTochatGPT(url, apiKey, model, maxTokens, question);
    }
}

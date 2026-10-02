package com.SemanticCaching.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class LlmClient {
    @Autowired
    private RestTemplate restTemplate;

    public String getAnswer(String query){
        String url="http://localhost:8002/LLM";
        Map<String,String> requestBody=new HashMap<>();
        requestBody.put("query",query);
        String response=restTemplate.postForObject(url,requestBody, Map.class).get("answer").toString();
        return response;

    }
}

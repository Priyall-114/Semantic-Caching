package com.SemanticCaching.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmbeddingClient {

    @Autowired
    private RestTemplate restTemplate;

    public List<Double> getEmbeddings(String query){
        String url="http://localhost:8002/embed";
        //Chhoti, safe values (IDs, keys, simple filenames) → query params mein theek hai, jaisa DocFlow mein tha
        //Free-form user text, lamba ya unpredictable content → request body mein bhejo, jaisa hum abhi kar rahe hain

        Map<String,String> requestBody=new HashMap<>();
        requestBody.put("text",query);
        Map response=restTemplate.postForObject(url,requestBody,Map.class);
        return (List<Double>) response.get("embedding");//Here now we have embeddings
        //Now these embeddings we will send to Redis vector search

    }


}

package com.SemanticCaching.services;

import com.SemanticCaching.Entity.response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import java.nio.charset.StandardCharsets;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;
@Component
public class RedisInteract {


    //Dependecy injection via constructor
    private final JedisPooled jedisPooled;


    public RedisInteract(JedisPooled jedisPooled) {
        this.jedisPooled = jedisPooled;
    }

    //Storing syntax
    //Key: cache:a1b2c3
    //├── field "text"      → value "how do I reset my UPI PIN"
    //└── field "embedding"  → value <raw bytes, 384 numbers packed together>


    public void storeEntry(String key, String text, List<Double> embedding,String answer,long ttlSeconds){
        byte[] embeddingBytes = floatListToBytes(embedding);


            Map<byte[],byte[]> hash=Map.of(
                    "text".getBytes(StandardCharsets.UTF_8),text.getBytes(StandardCharsets.UTF_8),
                    "embedding".getBytes(StandardCharsets.UTF_8),embeddingBytes,
                    "answer".getBytes(StandardCharsets.UTF_8),answer.getBytes(StandardCharsets.UTF_8)
            );
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            jedisPooled.hset(keyBytes,hash);
        jedisPooled.expire(keyBytes, ttlSeconds);

    }

    private byte[] floatListToBytes(List<Double> embedding) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(embedding.size() * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (Double value : embedding) {
            buffer.putFloat(value.floatValue());
        }
        return buffer.array();
    }

    public response similaritySearch(String user_query,List<Double> queryEmbedding, String IndexName){
        byte[] current_query_embeddingBytes = floatListToBytes(queryEmbedding);
            //! The smaller the score(which is distnace) the better
            Query query=new Query("*=>[KNN 1 @embedding $vec AS score]")
                    .addParam("vec",current_query_embeddingBytes)
                    .setSortBy("score",true)
                    .returnFields("text","score","answer")
                    .dialect(2);
            SearchResult result =jedisPooled.ftSearch(IndexName,query);

            if (result.getTotalResults()==0){

                //New query we need to store in Redis this then
                return null;
            }

        Document doc = result.getDocuments().get(0);
        String matchedText = doc.getString("text");
        String answer      = doc.getString("answer");
        double score       = Double.parseDouble(doc.getString("score"));
            response response=new response();
        response.setAnswer(answer);
        response.setScore(score);

        response.setQuery(user_query);
        //response.setLatency
            return response;

    }


}
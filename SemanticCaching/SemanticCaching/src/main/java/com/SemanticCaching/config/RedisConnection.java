package com.SemanticCaching.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConnection {


//    public static void main(String[] args) {
//        Jedis jedis=new Jedis("localhost",6379);
//        String pong =jedis.ping();
//        System.out.println(pong);
//    }

    //Now since we have created it's bean in here it becomes spring handled
    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled("localhost", 6379);
    }
}

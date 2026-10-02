package com.SemanticCaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SemanticCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemanticCachingApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}



}

package com.SemanticCaching.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix="semantic-cache")//"Create an object of this class and fill it using properties that begin with semantic-cache."
public class CacheRouting {
    private Map<String, RouteSettings> routes;

    public Map<String,RouteSettings> getRoutes(){
        return routes;
    }
    public void setRoutes(Map<String,RouteSettings> routes){
        this.routes=routes;
    }
    @Data
    public static  class RouteSettings{
        private double similarityThreshold;
        private long ttlSeconds;
    }

}



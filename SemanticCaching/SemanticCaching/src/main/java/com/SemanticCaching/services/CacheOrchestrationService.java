package com.SemanticCaching.services;

import com.SemanticCaching.Entity.response;
import com.SemanticCaching.config.CacheRouting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@Service
public class CacheOrchestrationService {
    @Autowired
    private CacheRouting cacheRouting;

    private static final Logger log = LoggerFactory.getLogger(CacheOrchestrationService.class);
    private final EmbeddingClient embeddingClient;
    private final RedisInteract redisInteract;
    private final LlmClient llmClient;


    public CacheRouting.RouteSettings getSettingsforRoute(String RouteId){
        Map<String,CacheRouting.RouteSettings> allRoutes=cacheRouting.getRoutes();

        CacheRouting.RouteSettings specificAttributes=allRoutes.get(RouteId);
       if(specificAttributes==null){
           throw new IllegalArgumentException("No cache config found for route "+RouteId);
       }
       return specificAttributes;
    }

    public CacheOrchestrationService(CacheRouting cacheRouting,EmbeddingClient embeddingClient,RedisInteract redisInteract,LlmClient llmClient){
        this.cacheRouting = cacheRouting;
        this.embeddingClient = embeddingClient;
        this.redisInteract = redisInteract;
        this.llmClient=llmClient;

    }
public response handleQuery(String routeId,String queryText){
    long startTime = System.currentTimeMillis();

    CacheRouting.RouteSettings routeSettings=getSettingsforRoute(routeId);
        List<Double> query_embeddings=embeddingClient.getEmbeddings(queryText);
        String indexName="cache_idx_" +
                ""+routeId;
        //We search via index name
        response searchResult=redisInteract.similaritySearch(queryText,query_embeddings,indexName);

    log.info("route={} query=\"{}\" nearestScore={} threshold={}",
            routeId,
            queryText,
            searchResult != null ? searchResult.getScore() : null,
            routeSettings.getSimilarityThreshold());
    if (searchResult != null && searchResult.getScore() <= routeSettings.getSimilarityThreshold()){
            //It is a cache hit
            searchResult.setCacheHit(true);
        searchResult.setLatnecyMs(System.currentTimeMillis() - startTime);
            return  searchResult;

    }
        else{
            String answer_from_llm= llmClient.getAnswer(queryText);
            //My preix1:cache
        //We store via prefix
            redisInteract.storeEntry(routeId+":"+java.util.UUID.randomUUID(),queryText,query_embeddings,answer_from_llm,routeSettings.getTtlSeconds());
            //I need some key but whyy? nowhere used
        response result=new response();
            result.setQuery(queryText);
        result.setCacheHit(false);
        result.setAnswer(answer_from_llm);
        result.setLatnecyMs(System.currentTimeMillis() - startTime);
        result.setScore(null);

            return result;
        }


}

}

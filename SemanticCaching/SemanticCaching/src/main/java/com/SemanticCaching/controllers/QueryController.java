package com.SemanticCaching.controllers;

import com.SemanticCaching.Entity.Query;
import com.SemanticCaching.Entity.response;
import com.SemanticCaching.config.CacheRouting;
import com.SemanticCaching.services.CacheOrchestrationService;
import com.SemanticCaching.services.EmbeddingClient;
import com.SemanticCaching.services.LlmClient;
import com.SemanticCaching.services.RedisInteract;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/L1/query")
public class QueryController {
    @Autowired
    private CacheOrchestrationService cacheOrchestrationService;



    @PostMapping("/request/{routeId}")
    public response query(@PathVariable String routeId, @Valid @RequestBody Query query){

        response result= cacheOrchestrationService.handleQuery(routeId,query.getQuery());

        return  result;
    }

}

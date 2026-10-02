package com.SemanticCaching.Entity;

import lombok.Data;

@Data
public class response {
    String query;
    Double Score;
    String answer;
    boolean cacheHit;
    long latnecyMs;
}

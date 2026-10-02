package com.SemanticCaching.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class Query {
    @NotBlank(message = "query Text cannot be empty ")
    String query;

}

package com.teutsch.ragassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public EmbeddingService(
            @Value("${openai.api.key}") String apiKey,
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public List<Double> generateEmbedding(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text for embedding cannot be empty.");
        }

        Map<String, Object> requestBody = Map.of(
                "model", "text-embedding-3-small",
                "input", text,
                "encoding_format", "float"
        );

        JsonNode response = restClient.post()
                .uri("/embeddings")
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        if (response == null || !response.has("data") || response.get("data").isEmpty()) {
            throw new RuntimeException("Failed to generate embedding: No data returned from OpenAI API.");
        }

        JsonNode embeddingNode = response
                .get("data")
                .get(0)
                .get("embedding");

        return objectMapper.convertValue(
                embeddingNode,
                new TypeReference<List<Double>>() {}
        );
    }

}

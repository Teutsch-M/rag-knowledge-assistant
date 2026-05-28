package com.teutsch.ragassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiChatService {

    private final RestClient restClient;

    public OpenAiChatService(@Value("${openai.api.key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String generateAnswer(String question, String context) {
        String systemPrompt = """
                You are an enterprise knowledge assistant.
                Answer the user's question using only the provided context.
                If the answer cannot be found in the context, say that you do not have enough information.
                Do not invent facts.
                Keep the answer clear and concise.
                """;

        String userPrompt = """
                Context:
                %s
                
                Question:
                %s
                """.formatted(context, question);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "temperature", 0.2,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", systemPrompt
                        ),
                        Map.of(
                                "role", "user",
                                "content", userPrompt
                        )
                )
        );

        JsonNode response = restClient.post()
                .uri("/chat/completions")
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        if (response == null || !response.has("choices") || response.get("choices").isEmpty()) {
            throw new RuntimeException("Invalid response from OpenAI chat API.");
        }

        return response
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();
    }

}

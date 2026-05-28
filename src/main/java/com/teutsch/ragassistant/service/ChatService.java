package com.teutsch.ragassistant.service;

import com.teutsch.ragassistant.dto.ChatResponse;
import com.teutsch.ragassistant.dto.RetrievedChunkDto;
import com.teutsch.ragassistant.dto.SourceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final SemanticSearchService semanticSearchService;
    private final OpenAiChatService openAiChatService;


    public ChatService(SemanticSearchService semanticSearchService, OpenAiChatService openAiChatService) {
        this.semanticSearchService = semanticSearchService;
        this.openAiChatService = openAiChatService;
    }

    public ChatResponse ask(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question cannot be empty.");
        }

        List<RetrievedChunkDto> retrievedChunks = semanticSearchService.search(question);

        String context = buildContext(retrievedChunks);

        String answer = openAiChatService.generateAnswer(question, context);

        List<SourceDto> sources = retrievedChunks.stream()
                .map(chunk -> new SourceDto(
                        chunk.getId(),
                        chunk.getDocumentId(),
                        chunk.getChunkIndex(),
                        chunk.getContent(),
                        chunk.getSimilarity()
                )).toList();

        return new ChatResponse(answer, sources);
    }

    private String buildContext(List<RetrievedChunkDto> chunks) {
        StringBuilder contextBuilder = new StringBuilder();

        for (int i = 0; i < chunks.size(); i++) {
            RetrievedChunkDto chunk = chunks.get(i);

            contextBuilder
                    .append("Source ")
                    .append(i + 1)
                    .append(":\n")
                    .append(chunk.getContent())
                    .append("\n\n");
        }

        return contextBuilder.toString();
    }
}
package com.teutsch.ragassistant.service;

import com.teutsch.ragassistant.dto.RetrievedChunkDto;
import com.teutsch.ragassistant.repository.DocumentChunkVectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemanticSearchService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkVectorRepository documentChunkVectorRepository;


    public SemanticSearchService(EmbeddingService embeddingService, DocumentChunkVectorRepository documentChunkVectorRepository) {
        this.embeddingService = embeddingService;
        this.documentChunkVectorRepository = documentChunkVectorRepository;
    }

    public List<RetrievedChunkDto> search(String query) {
        List<Double> queryEmbedding = embeddingService.generateEmbedding(query);
        return documentChunkVectorRepository.findMostSimilarChunks(queryEmbedding, 5);
    }
}

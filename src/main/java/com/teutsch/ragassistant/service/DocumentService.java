package com.teutsch.ragassistant.service;

import com.teutsch.ragassistant.model.Document;
import com.teutsch.ragassistant.model.DocumentChunk;
import com.teutsch.ragassistant.repository.DocumentChunkRepository;
import com.teutsch.ragassistant.repository.DocumentChunkVectorRepository;
import com.teutsch.ragassistant.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final DocumentParserService documentParserService;
    private final TextChunkingService textChunkingService;
    private final EmbeddingService embeddingService;
    private final DocumentChunkVectorRepository documentChunkVectorRepository;

    public DocumentService(DocumentRepository documentRepository, DocumentChunkRepository documentChunkRepository, DocumentParserService documentParserService, TextChunkingService textChunkingService, EmbeddingService embeddingService, DocumentChunkVectorRepository documentChunkVectorRepository) {
        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.documentParserService = documentParserService;
        this.textChunkingService = textChunkingService;
        this.embeddingService = embeddingService;
        this.documentChunkVectorRepository = documentChunkVectorRepository;
    }

    public Document saveAndProcessDocument(MultipartFile file) {
        Document document = new Document();
        document.setFilename(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setSize(file.getSize());
        document.setUploadedAt(LocalDateTime.now());

        Document savedDocument = documentRepository.save(document);

        String extractedText = documentParserService.extractText(file);
        List<String> chunks = textChunkingService.chunkText(extractedText);

        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);

            if (chunkText == null || chunkText.isBlank()) {
                continue; // Skip empty chunks
            }

            DocumentChunk chunk = new DocumentChunk();
            chunk.setDocument(savedDocument);
            chunk.setChunkIndex(i);
            chunk.setContent(chunkText);

            DocumentChunk savedChunk = documentChunkRepository.save(chunk);

            List<Double> embedding = embeddingService.generateEmbedding(chunkText);

            documentChunkVectorRepository.updateEmbedding(
                    savedChunk.getId(),
                    embedding
            );
        }

        return savedDocument;
    }
}

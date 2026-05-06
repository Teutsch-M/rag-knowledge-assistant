package com.teutsch.ragassistant.service;

import com.teutsch.ragassistant.model.Document;
import com.teutsch.ragassistant.model.DocumentChunk;
import com.teutsch.ragassistant.repository.DocumentChunkRepository;
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

    public DocumentService(DocumentRepository documentRepository, DocumentChunkRepository documentChunkRepository, DocumentParserService documentParserService, TextChunkingService textChunkingService) {
        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.documentParserService = documentParserService;
        this.textChunkingService = textChunkingService;
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
            DocumentChunk chunk = new DocumentChunk();
            chunk.setDocument(savedDocument);
            chunk.setChunkIndex(i);
            chunk.setContent(chunks.get(i));
            documentChunkRepository.save(chunk);
        }

        return savedDocument;
    }
}

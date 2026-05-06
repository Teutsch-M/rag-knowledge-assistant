package com.teutsch.ragassistant.controller;

import com.teutsch.ragassistant.model.Document;
import com.teutsch.ragassistant.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file")MultipartFile file
    ) {
        Document savedDocument = documentService.saveAndProcessDocument(file);
        return ResponseEntity.ok(savedDocument);
    }

}

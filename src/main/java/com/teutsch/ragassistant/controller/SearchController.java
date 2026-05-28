package com.teutsch.ragassistant.controller;

import com.teutsch.ragassistant.dto.RetrievedChunkDto;
import com.teutsch.ragassistant.service.SemanticSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final SemanticSearchService semanticSearchService;


    public SearchController(SemanticSearchService semanticSearchService) {
        this.semanticSearchService = semanticSearchService;
    }

    @GetMapping("/api/search")
    public List<RetrievedChunkDto> search(@RequestParam String query) {
        return semanticSearchService.search(query);
    }
}

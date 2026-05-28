package com.teutsch.ragassistant.dto;

public class RetrievedChunkDto {

    private Long id;
    private Long documentId;
    private Integer chunkIndex;
    private String content;
    private Double similarity;

    public RetrievedChunkDto(Long id, Long documentId, Integer chunkIndex, String content, Double similarity) {
        this.id = id;
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.similarity = similarity;
    }

    public Long getId() {
        return id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public String getContent() {
        return content;
    }

    public Double getSimilarity() {
        return similarity;
    }
}
package com.teutsch.ragassistant.dto;

public class SourceDto {

    private Long chunkId;
    private Long documentId;
    private Integer chunkIndex;
    private String content;
    private Double similarity;

    public SourceDto(Long chunkId, Long documentId, Integer chunkIndex, String content, Double similarity) {
        this.chunkId = chunkId;
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.similarity = similarity;
    }

    public Long getChunkId() {
        return chunkId;
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

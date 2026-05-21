package com.teutsch.ragassistant.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DocumentChunkVectorRepository {

    private final JdbcTemplate jdbcTemplate;


    public DocumentChunkVectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateEmbedding(Long chunkId, List<Double> embedding) {
        String vectorLiteral = toVectorLiteral(embedding);

        String sql = """
                UPDATE document_chunks
                SET embedding = ?::vector
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, vectorLiteral, chunkId);
    }

    private String toVectorLiteral(List<Double> embedding) {
        return embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}

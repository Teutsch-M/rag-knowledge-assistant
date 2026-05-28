package com.teutsch.ragassistant.repository;

import com.teutsch.ragassistant.dto.RetrievedChunkDto;
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

    public List<RetrievedChunkDto> findMostSimilarChunks(List<Double> queryEmbedding, int limit) {
        String vectorLiteral = toVectorLiteral(queryEmbedding);

        String sql = """
                SELECT
                    id,
                    document_id,
                    chunk_index,
                    content,
                    1 - (embedding <=> ?::vector) AS similarity
                FROM document_chunks
                WHERE embedding IS NOT NULL
                ORDER BY embedding <=> ?::vector
                LIMIT ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->  new RetrievedChunkDto(
                        rs.getLong("id"),
                        rs.getLong("document_id"),
                        rs.getInt("chunk_index"),
                        rs.getString("content"),
                        rs.getDouble("similarity")
                ),
                vectorLiteral,
                vectorLiteral,
                limit
        );

    }

    private String toVectorLiteral(List<Double> embedding) {
        return embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}

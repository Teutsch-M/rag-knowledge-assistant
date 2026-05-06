package com.teutsch.ragassistant.repository;

import com.teutsch.ragassistant.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
}
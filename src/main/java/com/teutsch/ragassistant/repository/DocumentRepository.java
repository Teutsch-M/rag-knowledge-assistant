package com.teutsch.ragassistant.repository;

import com.teutsch.ragassistant.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
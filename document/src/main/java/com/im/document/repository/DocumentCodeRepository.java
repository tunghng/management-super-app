package com.im.document.repository;

import com.im.document.model.DocumentCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCodeRepository extends JpaRepository<DocumentCode, String> {
}

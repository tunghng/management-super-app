package com.im.news.service;

import com.im.news.model.NewsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCodeRepository extends JpaRepository<NewsCode, String> {
}

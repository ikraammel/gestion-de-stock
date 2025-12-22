package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Optional<Article> findByCodeArticle(String code);

    List<Article> findAllByCategoryId(Integer id);
}

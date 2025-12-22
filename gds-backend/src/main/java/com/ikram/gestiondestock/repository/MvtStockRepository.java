package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.MvtStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStockRepository extends JpaRepository<MvtStock,Integer> {
    @Query("select sum(m.quantite) FROM MvtStock m WHERE m.article.id = :articleId")
    BigDecimal stockReelArticle(Integer articleId);

    List<MvtStock> findAllByArticleId(Integer articleId);
}

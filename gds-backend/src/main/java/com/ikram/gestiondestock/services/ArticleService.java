package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.*;

import java.util.List;

public interface ArticleService {
    ArticleDto save(ArticleDto articleDto);
    ArticleDto findById(Integer id);
    ArticleDto findByCodeArticle(String code);
    List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);
    List<LigneCommandeClientDto> findHistoriqueCommandesClients(Integer idArticle);
    List<LigneCommandeFournisseurDto> findHistoriqueCommandesFournisseurs(Integer idArticle);
    List<ArticleDto> findAllArticlesByIdCategory(Integer idCategory);
    List<ArticleDto> findAll();
    void delete(Integer id);

}

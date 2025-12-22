package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.ArticleValidator;
import com.ikram.gestiondestock.dto.ArticleDto;
import com.ikram.gestiondestock.dto.LigneCommandeClientDto;
import com.ikram.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.ikram.gestiondestock.dto.LigneVenteDto;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.model.LigneCommandeClient;
import com.ikram.gestiondestock.model.LigneCommandeFournisseur;
import com.ikram.gestiondestock.model.LigneVente;
import com.ikram.gestiondestock.repository.*;
import com.ikram.gestiondestock.services.ArticleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class IArticleService implements ArticleService {

    private final ArticleRepository articleRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private final LigneCommandeClientRepository ligneCommandeClientRepository;

    @Override
    public ArticleDto save(ArticleDto articleDto) {
        List<String> errors = ArticleValidator.validate(articleDto);
        if(!errors.isEmpty()){
            log.error("Article is not valid ",articleDto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID,errors);
        }
        return ArticleDto.fromEntity(articleRepository.save(ArticleDto.toEntity(articleDto)));
    }

    @Override
    public ArticleDto findById(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }

        return articleRepository.findById(id)
                .map(ArticleDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun article avec l'id " + id + " n'a été trouvé",
                        ErrorCodes.ARTICLE_NOT_FOUND
                ));
    }


    @Override
    public ArticleDto findByCodeArticle(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Article Code is null");
            return null;
        }

        return articleRepository.findByCodeArticle(code)
                .map(ArticleDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun article avec le code " + code + " n'a été trouvé",
                        ErrorCodes.ARTICLE_NOT_FOUND
                ));
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
        return ligneVenteRepository.findAllByArticleId(idArticle).stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandesClients(Integer idArticle) {
        return ligneCommandeClientRepository.findAllByArticleId(idArticle).stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandesFournisseurs(Integer idArticle) {
        return ligneCommandeFournisseurRepository.findAllByArticleId(idArticle).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public List<ArticleDto> findAllArticlesByIdCategory(Integer idCategory) {
        return articleRepository.findAllByCategoryId(idCategory).stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Article ID is null");
            return;
        }

        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
        if (!ligneCommandeClients.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déjà utilisé dans des commandes clients",
                    ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }

        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(id);
        if (!ligneCommandeFournisseurs.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déjà utilisé dans des commandes fournisseurs",
                    ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }

        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByArticleId(id);
        if (!ligneVentes.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déjà utilisé dans des ventes",
                    ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }

        articleRepository.deleteById(id);
    }
}

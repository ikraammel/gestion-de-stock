package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.VentesValidator;
import com.ikram.gestiondestock.dto.ArticleDto;
import com.ikram.gestiondestock.dto.LigneVenteDto;
import com.ikram.gestiondestock.dto.MvtStockDto;
import com.ikram.gestiondestock.dto.VentesDto;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.model.*;
import com.ikram.gestiondestock.repository.ArticleRepository;
import com.ikram.gestiondestock.repository.LigneVenteRepository;
import com.ikram.gestiondestock.repository.VentesRepository;
import com.ikram.gestiondestock.services.MvmtStockService;
import com.ikram.gestiondestock.services.VentesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IVentesService implements VentesService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private VentesRepository ventesRepository;
    @Autowired
    private LigneVenteRepository ligneVenteRepository;
    @Autowired
    private MvmtStockService mvmtStockService;

    @Override
    public VentesDto save(VentesDto ventesDto) {
        List<String> errors = VentesValidator.validate(ventesDto);
        if (!errors.isEmpty()) {
            log.error("L'objet ventes n'est pas valide");
            throw new InvalidEntityException("", ErrorCodes.VENTE_NOT_VALID);
        }

        List<String> articleErrors = new ArrayList<>();
        ventesDto.getLigneVenteDtos().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'id" + ligneVenteDto.getArticle().getId() + " n'a été trouvé dans la bd");
            }
        });
        if (!articleErrors.isEmpty()) {
            log.error("Un ou plusieurs articles n'ont pas été trouvés dans la bd, {}", errors);
            throw new InvalidEntityException("Un ou plusieurs articles n'ont pas été trouvés dans la bd", ErrorCodes.VENTE_NOT_VALID);
        }

        Ventes savedVentes = ventesRepository.save(VentesDto.toEntity(ventesDto));
        ventesDto.getLigneVenteDtos().forEach(ligneVenteDto -> {
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVentes(savedVentes);
            ligneVenteRepository.save(ligneVente);
            updateMvtStock(ligneVente);
        });
        return VentesDto.fromEntity(savedVentes);
    }

    @Override
    public VentesDto findById(Integer id) {
        if(id == null){
            log.error("Ventes ID is null");
            return null;
        }
        return ventesRepository.findById(id)
                .map(VentesDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucune vente n'a été trouvée dans la bd",ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public VentesDto findByCode(String code) {
        if(!StringUtils.hasLength(code)){
            log.error("Ventes code is null");
            return null;
        }
        return ventesRepository.findByCode(code)
                .map(VentesDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucune vente n'a été trouvée dans la bd",ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public List<VentesDto> findAll() {
        return ventesRepository.findAll().stream()
                .map(VentesDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Venteis NULL");
            return;
        }
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVentesId(id);

        if (!ligneVentes.isEmpty()) {
            throw new InvalidEntityException(
                    "Impossible de supprimer une vente qui a déjà des lignes ventes",
                    ErrorCodes.VENTE_ALREADY_IN_USE
            );
        }
        ventesRepository.deleteById(id);
    }

    private void updateMvtStock(LigneVente ligneVente){

            MvtStockDto mvtStockDto = MvtStockDto.builder()
                    .article(ArticleDto.fromEntity(ligneVente.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStock(TypeMvtStock.SORTIE)
                    .sourceMvtStock(SourceMvtStock.VENTE)
                    .quantite(ligneVente.getQuantite())
                    .entrepriseId(ligneVente.getEntrepriseId())
                    .build();
            mvmtStockService.sortieStock(mvtStockDto);
    }
}

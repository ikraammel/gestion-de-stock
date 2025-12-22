package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.ArticleValidator;
import com.ikram.gestiondestock.Validator.CommandeFournisseurValidator;
import com.ikram.gestiondestock.dto.*;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.model.*;
import com.ikram.gestiondestock.repository.*;
import com.ikram.gestiondestock.services.CommandeFournisseurService;
import com.ikram.gestiondestock.services.MvmtStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ICommandeFournisseurService implements CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final ArticleRepository articleRepository;
    private final FournisseurRepository fournisseurRepository;
    private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private final MvmtStockService mvmtStockService;

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto) {
        List<String> errors = CommandeFournisseurValidator.validate(commandeFournisseurDto);

        if (!errors.isEmpty()){
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND,errors);
        }

            Optional<Fournisseur> fournisseur = fournisseurRepository.findById(commandeFournisseurDto.getFournisseur().getId());
        if (!fournisseur.isPresent()){
            log.warn("Fournisseur with id {} was not found in the db",commandeFournisseurDto.getFournisseur().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec l'id"+commandeFournisseurDto.getFournisseur().getId()+ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }
        List<String> articleErrors = new ArrayList<>();

        if( commandeFournisseurDto.getFournisseur().getId() != null){
            commandeFournisseurDto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {

                Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
                if (article.isEmpty()){
                    articleErrors.add("L'article avec l'id"+ligCmdFrs.getArticle().getId() +"n'existe plus");
                }
            });
        }else{
            articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
        }
        if (!articleErrors.isEmpty()){
            log.warn("");
            throw new InvalidEntityException("",ErrorCodes.ARTICLE_NOT_FOUND,articleErrors);
        }
        CommandeFournisseur savedCmdClt = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto));
        if (commandeFournisseurDto.getLigneCommandeFournisseurs() != null) {
            commandeFournisseurDto.getLigneCommandeFournisseurs().forEach(ligneCmdFrs -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligneCmdFrs);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCmdClt);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
            });
        }
        return CommandeFournisseurDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("L'état de la commande fournisseur est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un état null", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
        commandeFournisseurDto.setEtatCommande(etatCommande);

        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto));
        if (commandeFournisseurDto.isCommandeLivree()){
            updateMvtStock(idCommande);
        }
        return CommandeFournisseurDto.fromEntity(savedCommande);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null && quantite.compareTo(BigDecimal.ZERO) == 0){
            log.error("Ligne commande fournisseur id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une quantité null ou zero", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findById(idLigneCommande)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune ligne commande fournisseur trouvée avec l'id"+idLigneCommande,
                        ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND
                ));

        ligneCommandeFournisseur.setQuantite(quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseurDto;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        checkIdCommande(idCommande);
        if (idFournisseur == null){
            log.error("Fournisseur id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un id fournisseur null", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);

        Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur)
                .orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur trouvé avec l'id"+idFournisseur,ErrorCodes.FOURNISSEUR_NOT_FOUND));

        commandeFournisseurDto.setFournisseur(FournisseurDto.fromEntity(fournisseur));

        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto))
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle,"nouveau");

        CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findById(idLigneCommande)
                .orElseThrow(() -> new EntityNotFoundException("Aucune ligne commande trouvée avec l'id"+idLigneCommande));

        Article article = articleRepository.findById(idArticle)
                .orElseThrow(() -> new EntityNotFoundException("Aucun article trouvé avec l'id"+idArticle,
                        ErrorCodes.ARTICLE_NOT_FOUND));

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(article));
        if(!errors.isEmpty()){
            throw new InvalidEntityException("",ErrorCodes.ARTICLE_NOT_VALID,errors);
        }

        ligneCommandeFournisseur.setArticle(article);
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
        return commandeFournisseurDto;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findById(idLigneCommande)
                .orElseThrow(() -> new EntityNotFoundException("Aucune ligne commande trouvée avec l'id"+idLigneCommande));
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);
        return commandeFournisseurDto;
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if (id == null){
            log.error("Commande fournisseur Id is null");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a été trouvé avec l'id "+id,ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public CommandeFournisseurDto findByCode(String code) {
        if(!StringUtils.hasLength(code)){
            log.error("Commande fournisseur Code is null");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a été trouvé avec le code "+code,ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll()
                .stream().map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Commande fournisseur is NULL");
            return;
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(id);
        if (!ligneCommandeFournisseurs.isEmpty()){
            throw new InvalidEntityException(
                    "Impossible de supprimer une commande fournisseur déjà utilisée",
                    ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE
            );
        }
        commandeFournisseurRepository.deleteById(id);
    }

    private void checkIdCommande(Integer idCommande){
        if (idCommande == null){
            log.error("Commande fournisseur id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un id null", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande){
        if (idLigneCommande == null){
            log.error("Ligne commande fournisseur id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une ligne de commande id null", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle,String msg){
        if (idArticle == null){
            log.error("L'id de "+msg+ "article est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un"+msg+ "id article null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private CommandeFournisseurDto checkEtatCommande(Integer idCommande){
        CommandeFournisseurDto commandeFournisseurDto = findById(idCommande);
        if (commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Impossible de modifier une commande déjà livrée", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return commandeFournisseurDto;
    }

    private void updateMvtStock(Integer commandeId){
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(commandeId);
        ligneCommandeFournisseurs.forEach(lig -> {
            MvtStockDto mvtStockDto = MvtStockDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStock(TypeMvtStock.SORTIE)
                    .sourceMvtStock(SourceMvtStock.COMMANDE_FOURNISSEUR)
                    .quantite(lig.getQuantite())
                    .entrepriseId(lig.getEntrepriseId())
                    .build();
            mvmtStockService.entreeStock(mvtStockDto);
        });
    }
}

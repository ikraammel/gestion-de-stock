package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.ArticleValidator;
import com.ikram.gestiondestock.Validator.CommandeClientValidator;
import com.ikram.gestiondestock.dto.*;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.model.*;
import com.ikram.gestiondestock.repository.ArticleRepository;
import com.ikram.gestiondestock.repository.ClientRepository;
import com.ikram.gestiondestock.repository.CommandeClientRepository;
import com.ikram.gestiondestock.repository.LigneCommandeClientRepository;
import com.ikram.gestiondestock.services.CommandeClientService;
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
public class ICommandeClientService implements CommandeClientService {

    private final CommandeClientRepository commandeClientRepository;
    private final ArticleRepository articleRepository;
    private final ClientRepository clientRepository;
    private final LigneCommandeClientRepository ligneCommandeClientRepository;
    private final MvmtStockService mvmtStockService;

    @Override
    public CommandeClientDto save(CommandeClientDto commandeClientDto) {
        List<String> errors = CommandeClientValidator.validate(commandeClientDto);

        if (!errors.isEmpty()) {
            log.error("Commande client n'est pas valide");
            throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_FOUND, errors);
        }

        if (commandeClientDto != null && commandeClientDto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier une commande déjà livrée", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(commandeClientDto.getClient().getId());
        if (!client.isPresent()) {
            log.warn("Client with id {} was not found in the db", commandeClientDto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'id" + commandeClientDto.getClient().getId() + ErrorCodes.CLIENT_NOT_FOUND);
        }
        List<String> articleErrors = new ArrayList<>();

        if (commandeClientDto.getClient().getId() != null) {
            commandeClientDto.getLigneCommandeClient().forEach(ligCmdClt -> {

                Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                if (article.isEmpty()) {
                    articleErrors.add("L'article avec l'id" + ligCmdClt.getArticle().getId() + "n'existe plus");
                }
            });
        } else {
            articleErrors.add("Impossible d'enregsitrer une commande avec un article NULL");
        }
        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }
        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto));
        if (commandeClientDto.getLigneCommandeClient() != null) {
            commandeClientDto.getLigneCommandeClient().forEach(ligneCmdClt -> {
                LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligneCmdClt);
                ligneCommandeClient.setCommandeClient(savedCmdClt);
                ligneCommandeClientRepository.save(ligneCommandeClient);
            });
        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("L'état de la commande client est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un état null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);
        commandeClientDto.setEtatCommande(etatCommande);

        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto));
        if (commandeClientDto.isCommandeLivree()){
            updateMvtStock(idCommande);
        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        if (quantite == null && quantite.compareTo(BigDecimal.ZERO) == 0){
            log.error("Ligne commande client id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une quantité null ou zero", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientRepository.findById(idLigneCommande)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune ligne commande client trouvée avec l'id"+idLigneCommande,
                        ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND
                ));

        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClientDto;
    }

    @Override
    public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {
        checkIdCommande(idCommande);
        if (idClient == null){
            log.error("Client id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un id client null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);

        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new EntityNotFoundException("Aucun client trouvé avec l'id"+idClient,ErrorCodes.CLIENT_NOT_FOUND));

        commandeClientDto.setClient(ClientDto.fromEntity(client));

        return CommandeClientDto.fromEntity(
                commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto))
        );
    }

    @Override
    public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande,Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle,"nouveau");

        CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientRepository.findById(idLigneCommande)
                .orElseThrow(() -> new EntityNotFoundException("Aucune ligne commande trouvée avec l'id"+idLigneCommande));

        Article article = articleRepository.findById(idArticle)
                .orElseThrow(() -> new EntityNotFoundException("Aucun article trouvé avec l'id"+idArticle,
                        ErrorCodes.ARTICLE_NOT_FOUND));

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(article));
        if(!errors.isEmpty()){
            throw new InvalidEntityException("",ErrorCodes.ARTICLE_NOT_VALID,errors);
        }

        ligneCommandeClient.setArticle(article);
        ligneCommandeClientRepository.save(ligneCommandeClient);
        return commandeClientDto;
    }

    @Override
    public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientRepository.findById(idLigneCommande)
                .orElseThrow(() -> new EntityNotFoundException("Aucune ligne commande trouvée avec l'id"+idLigneCommande));
        ligneCommandeClientRepository.deleteById(idLigneCommande);
        return commandeClientDto;
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        if (id == null) {
            log.error("Commande client Id is null");
            return null;
        }
        return commandeClientRepository.findById(id)
                .map(CommandeClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a été trouvé avec l'id " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public CommandeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande client Code is null");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
                .map(CommandeClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a été trouvé avec le code " + code, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll()
                .stream().map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
        return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande client is NULL");
            return;
        }
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
        if (!ligneCommandeClients.isEmpty()){
            throw new InvalidEntityException(
                    "Impossible de supprimer une commande client déjà utilisée",
                    ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE
            );
        }
        commandeClientRepository.deleteById(id);
    }

    private void checkIdCommande(Integer idCommande){
        if (idCommande == null){
            log.error("Commande client id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un id null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande){
        if (idLigneCommande == null){
            log.error("Ligne commande client id is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec une ligne de commande id null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle,String msg){
        if (idArticle == null){
            log.error("L'id de "+msg+ "article est null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un"+msg+ "id article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private CommandeClientDto checkEtatCommande(Integer idCommande){
        CommandeClientDto commandeClientDto = findById(idCommande);
        if (commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Impossible de modifier une commande déjà livrée", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        return commandeClientDto;
    }

    private void updateMvtStock(Integer commandeId){
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(commandeId);
        ligneCommandeClients.forEach(lig -> {
            MvtStockDto mvtStockDto = MvtStockDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStock(TypeMvtStock.SORTIE)
                    .sourceMvtStock(SourceMvtStock.COMMANDE_CLIENT)
                    .quantite(lig.getQuantite())
                    .entrepriseId(lig.getEntrepriseId())
                    .build();
            mvmtStockService.sortieStock(mvtStockDto);
        });
    }
}


package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.CommandeClientDto;
import com.ikram.gestiondestock.dto.LigneCommandeClientDto;
import com.ikram.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {
    CommandeClientDto save(CommandeClientDto commandeClientDto);
    CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);
    CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);
    CommandeClientDto updateClient(Integer idCommande, Integer idClient);
    CommandeClientDto updateArticle(Integer idCommande,Integer idLigneCommande,Integer idArticle);
    //delete article => delete ligneCommandeClient
    CommandeClientDto deleteArticle(Integer idCommande,Integer idLigneCommande);
    CommandeClientDto findById(Integer id);
    CommandeClientDto findByCode(String code);
    List<CommandeClientDto> findAll();
    List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);
    void delete(Integer id);
}

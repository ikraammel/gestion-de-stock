package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.CommandeFournisseurDto;
import com.ikram.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.ikram.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {
    CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto);
    CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);
    CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);
    CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);
    CommandeFournisseurDto updateArticle(Integer idCommande,Integer idLigneCommande,Integer idArticle);
    //delete article => delete ligneCommandeFournisseur
    CommandeFournisseurDto deleteArticle(Integer idCommande,Integer idLigneCommande);
    CommandeFournisseurDto findById(Integer id);
    CommandeFournisseurDto findByCode(String code);
    List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande);
    List<CommandeFournisseurDto> findAll();
    void delete(Integer id);
}

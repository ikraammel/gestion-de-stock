package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur,Integer> {
    List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Integer fournisseurId);
    List<LigneCommandeFournisseur> findAllByArticleId(Integer id);
}

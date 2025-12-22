package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur,Integer> {
    List<CommandeFournisseur> findAllByFournisseurId(Integer id);

    Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);
}

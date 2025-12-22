package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer> {
    Optional<Utilisateur> findUtilisateurByEmail(String email);

    Optional<Utilisateur> findByEmail(String email);
}

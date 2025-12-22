package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeClientRepository extends JpaRepository<CommandeClient,Integer> {
    List<CommandeClient> findAllByClientId(Integer id);

    Optional<CommandeClient> findCommandeClientByCode(String code);
}

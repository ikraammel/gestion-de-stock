package com.ikram.gestiondestock.repository;

import com.ikram.gestiondestock.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneVenteRepository extends JpaRepository<LigneVente,Integer> {
    List<LigneVente> findAllByArticleId(Integer articleId);

    List<LigneVente> findAllByVentesId(Integer id);
}

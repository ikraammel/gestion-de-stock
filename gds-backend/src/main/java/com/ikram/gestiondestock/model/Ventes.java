package com.ikram.gestiondestock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Ventes extends AbstractEntity{
    private String code;
    private Instant dateVente;
    private String commentaire;

    @OneToMany(mappedBy = "ventes")
    private List<LigneVente> ligneVentes;

    private Integer entrepriseId;
}

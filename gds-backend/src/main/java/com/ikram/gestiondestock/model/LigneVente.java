package com.ikram.gestiondestock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class LigneVente extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "ventesId")
    private Ventes ventes;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    private BigDecimal quantite;
    private BigDecimal prixUnitaire;

    private Integer entrepriseId;
}

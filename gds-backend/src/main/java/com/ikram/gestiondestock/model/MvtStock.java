package com.ikram.gestiondestock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MvtStock extends AbstractEntity{
    private Instant dateMvt;
    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    private TypeMvtStock typeMvtStock;

    private SourceMvtStock sourceMvtStock;

    private Integer entrepriseId;
}

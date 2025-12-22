package com.ikram.gestiondestock.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Adresse {
    private String adresse1;
    private String adresse2;
    private String ville;
    private String codePostale;
    private String pays;
}

package com.ikram.gestiondestock.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Entreprise extends AbstractEntity{
    private String nom;
    private String description;

    @Embedded
    private Adresse adresse;

    private String codeFiscal;
    private String photo;
    private String email;
    private String numTel;
    private String siteWeb;

    @OneToMany(mappedBy = "entreprise")
    private List<Utilisateur> utilisateurs;
}

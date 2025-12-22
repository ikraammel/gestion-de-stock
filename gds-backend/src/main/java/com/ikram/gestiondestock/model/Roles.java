package com.ikram.gestiondestock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Roles extends AbstractEntity{
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "utilisateurId")
    private Utilisateur utilisateur;
}

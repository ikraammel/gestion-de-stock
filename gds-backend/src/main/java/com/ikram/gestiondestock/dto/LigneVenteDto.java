package com.ikram.gestiondestock.dto;

import com.ikram.gestiondestock.model.LigneVente;
import com.ikram.gestiondestock.model.Ventes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LigneVenteDto {
    private Integer id;
    private VentesDto vente;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private ArticleDto article;
    private Integer entrepriseId;

    public static LigneVenteDto fromEntity(LigneVente ligneVente) {
        if (ligneVente == null) {
            return null;
        }

        return LigneVenteDto.builder()
                .id(ligneVente.getId())
                .vente(VentesDto.fromEntity(ligneVente.getVentes()))
                .article(ArticleDto.fromEntity(ligneVente.getArticle()))
                .quantite(ligneVente.getQuantite())
                .prixUnitaire(ligneVente.getPrixUnitaire())
                .entrepriseId(ligneVente.getEntrepriseId())
                .build();
    }

    public static LigneVente toEntity(LigneVenteDto dto) {
        if (dto == null) {
            return null;
        }
        LigneVente ligneVente = new LigneVente();
        ligneVente.setId(dto.getId());
        ligneVente.setVentes(VentesDto.toEntity(dto.getVente()));
        ligneVente.setArticle(ArticleDto.toEntity(dto.getArticle()));
        ligneVente.setQuantite(dto.getQuantite());
        ligneVente.setPrixUnitaire(dto.getPrixUnitaire());
        ligneVente.setEntrepriseId(dto.getEntrepriseId());
        return ligneVente;
    }
}

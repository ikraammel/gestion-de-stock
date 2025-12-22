package com.ikram.gestiondestock.dto;

import com.ikram.gestiondestock.model.Ventes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentesDto {
    private Integer id;
    private String code;
    private Instant dateVente;
    private String commentaire;
    private Integer entrepriseId;
    private List<LigneVenteDto> ligneVenteDtos;

    public static VentesDto fromEntity(Ventes vente) {
        if (vente == null) {
            return null;
        }
        return VentesDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .commentaire(vente.getCommentaire())
                .entrepriseId(vente.getEntrepriseId())
                .build();
    }

    public static Ventes toEntity(VentesDto dto) {
        if (dto == null) {
            return null;
        }
        Ventes ventes = new Ventes();
        ventes.setId(dto.getId());
        ventes.setCode(ventes.getCode());
        ventes.setCommentaire(dto.getCommentaire());
        ventes.setEntrepriseId(dto.getEntrepriseId());
        return ventes;
    }
}

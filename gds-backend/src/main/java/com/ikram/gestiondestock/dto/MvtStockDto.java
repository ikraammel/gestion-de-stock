package com.ikram.gestiondestock.dto;

import com.ikram.gestiondestock.model.MvtStock;
import com.ikram.gestiondestock.model.SourceMvtStock;
import com.ikram.gestiondestock.model.TypeMvtStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MvtStockDto {
    private Integer id;
    private Instant dateMvt;
    private BigDecimal quantite;
    private ArticleDto article;
    private TypeMvtStock typeMvtStock;
    private Integer entrepriseId;
    private SourceMvtStock sourceMvtStock;


    public static MvtStockDto fromEntity(MvtStock mvtStk) {
        if (mvtStk == null) {
            return null;
        }

        return MvtStockDto.builder()
                .id(mvtStk.getId())
                .dateMvt(mvtStk.getDateMvt())
                .quantite(mvtStk.getQuantite())
                .article(ArticleDto.fromEntity(mvtStk.getArticle()))
                .typeMvtStock(mvtStk.getTypeMvtStock())
                .sourceMvtStock(mvtStk.getSourceMvtStock())
                .entrepriseId(mvtStk.getEntrepriseId())
                .build();
    }

    public static MvtStock toEntity(MvtStockDto dto) {
        if (dto == null) {
            return null;
        }

        MvtStock mvtStk = new MvtStock();
        mvtStk.setId(dto.getId());
        mvtStk.setDateMvt(dto.getDateMvt());
        mvtStk.setQuantite(dto.getQuantite());
        mvtStk.setArticle(ArticleDto.toEntity(dto.getArticle()));
        mvtStk.setTypeMvtStock(dto.getTypeMvtStock());
        mvtStk.setEntrepriseId(dto.getEntrepriseId());
        mvtStk.setSourceMvtStock(dto.getSourceMvtStock());
        return mvtStk;
    }
}

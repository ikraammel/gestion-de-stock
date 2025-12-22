package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.MvmtStockValidator;
import com.ikram.gestiondestock.dto.MvtStockDto;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.model.TypeMvtStock;
import com.ikram.gestiondestock.repository.MvtStockRepository;
import com.ikram.gestiondestock.services.ArticleService;
import com.ikram.gestiondestock.services.MvmtStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IMvmtStockService implements MvmtStockService {

    private final MvtStockRepository mvtStockRepository;
    private final ArticleService articleService;

    @Override
    public BigDecimal stockReelArticle(Integer articleId) {
        if (articleId == null){
            log.warn("Article id is null");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(articleId);
        return mvtStockRepository.stockReelArticle(articleId);
    }

    @Override
    public List<MvtStockDto> mvtStockArticle(Integer articleId) {
        return mvtStockRepository.findAllByArticleId(articleId).stream()
                .map(MvtStockDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MvtStockDto entreeStock(MvtStockDto mvtStockDto) {
        return entreePositive(mvtStockDto,TypeMvtStock.ENTREE);
    }

    @Override
    public MvtStockDto sortieStock(MvtStockDto mvtStockDto) {
        return sortieNegative(mvtStockDto,TypeMvtStock.SORTIE);
    }

    @Override
    public MvtStockDto correctionStockPos(MvtStockDto mvtStockDto) {
        return entreePositive(mvtStockDto,TypeMvtStock.CORRECTION_POS);
    }

    @Override
    public MvtStockDto correctionStockNeg(MvtStockDto mvtStockDto) {
        return sortieNegative(mvtStockDto,TypeMvtStock.CORRECTION_NEG);
    }

    private MvtStockDto entreePositive(MvtStockDto mvtStockDto,TypeMvtStock typeMvtStock){
        List<String> errors = MvmtStockValidator.validate(mvtStockDto);
        if (!errors.isEmpty()){
            log.error("Mvmt stock id non valide ! {}",mvtStockDto);
            throw new InvalidEntityException("Le mvmt de stock n'est pas valide", ErrorCodes.MVT_STOCK_NOT_VALID,errors);
        }
        mvtStockDto.setQuantite(BigDecimal.valueOf(Math.abs(mvtStockDto.getQuantite().doubleValue())));
        mvtStockDto.setTypeMvtStock(TypeMvtStock.ENTREE);
        return MvtStockDto.fromEntity(mvtStockRepository.save(MvtStockDto.toEntity(mvtStockDto)));
    }

    private MvtStockDto sortieNegative(MvtStockDto mvtStockDto,TypeMvtStock typeMvtStock){
        List<String> errors = MvmtStockValidator.validate(mvtStockDto);
        if (!errors.isEmpty()){
            log.error("Mvmt stock id non valide ! {}",mvtStockDto);
            throw new InvalidEntityException("Le mvmt de stock n'est pas valide", ErrorCodes.MVT_STOCK_NOT_VALID,errors);
        }
        mvtStockDto.setQuantite(BigDecimal.valueOf(Math.abs(mvtStockDto.getQuantite().doubleValue()) * -1));
        mvtStockDto.setTypeMvtStock(typeMvtStock);
        return MvtStockDto.fromEntity(mvtStockRepository.save(MvtStockDto.toEntity(mvtStockDto)));
    }
}

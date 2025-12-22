package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.MvtStockDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvmtStockService {
    BigDecimal stockReelArticle(Integer articleId);
    List<MvtStockDto> mvtStockArticle(Integer articleId);
    MvtStockDto entreeStock(MvtStockDto mvtStockDto);
    MvtStockDto sortieStock(MvtStockDto mvtStockDto);
    MvtStockDto correctionStockPos(MvtStockDto mvtStockDto);
    MvtStockDto correctionStockNeg(MvtStockDto mvtStockDto);
}

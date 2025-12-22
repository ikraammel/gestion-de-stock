package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.MvtStockDto;
import com.ikram.gestiondestock.services.MvmtStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;
@RestController
@RequestMapping(APP_ROOT + "/mvmtStock")
public class MvmtStockController {

    @Autowired
    private MvmtStockService mvmtStockService;

    @GetMapping("/stockreel/{articleId}")
    public BigDecimal stockReelArticle(@PathVariable Integer articleId){
        return mvmtStockService.stockReelArticle(articleId);
    }

    @GetMapping("/filter/article/{articleId}")
    List<MvtStockDto> mvtStockArticle(@PathVariable Integer articleId){
        return mvmtStockService.mvtStockArticle(articleId);
    }

    @PostMapping("/entree")
    MvtStockDto entreeStock(@RequestBody MvtStockDto mvtStockDto){
        return mvmtStockService.entreeStock(mvtStockDto);
    }

    @PostMapping("/sortie")
    MvtStockDto sortieStock(@RequestBody MvtStockDto mvtStockDto){
        return mvmtStockService.sortieStock(mvtStockDto);
    }

    @PostMapping("/correctionpos")
    MvtStockDto correctionStockPos(@RequestBody MvtStockDto mvtStockDto){
        return mvmtStockService.correctionStockPos(mvtStockDto);
    }

    @PostMapping("/correctionneg")
    MvtStockDto correctionStockNeg(@RequestBody MvtStockDto mvtStockDto){
        return mvmtStockService.correctionStockNeg(mvtStockDto);
    }
}

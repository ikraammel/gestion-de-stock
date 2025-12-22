package com.ikram.gestiondestock.Validator;

import com.ikram.gestiondestock.dto.MvtStockDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MvmtStockValidator {
    public static List<String> validate(MvtStockDto mvtStockDto) {
        List<String> errors = new ArrayList<>();
        if (mvtStockDto == null) {
            errors.add("Veuillez renseigner la date du mouvement");
            errors.add("Veuillez renseigner la quantité du mouvement");
            errors.add("Veuillez renseigner l'article");
            errors.add("Veuillez renseigner le type du mouvement");
            return errors;
        }
        if (mvtStockDto.getDateMvt() == null){
            errors.add("Veuillez renseigner la date du mouvement");
        }
        if (mvtStockDto.getQuantite() == null || mvtStockDto.getQuantite().compareTo(BigDecimal.ZERO) == 0){
            errors.add("Veuillez renseigner la quantité du mouvement");
        }
        if (mvtStockDto.getArticle() == null || mvtStockDto.getArticle().getId() == null){
            errors.add("Veuillez renseigner l'article");
        }
        if (!StringUtils.hasLength(mvtStockDto.getTypeMvtStock().name())){
            errors.add("Veuillez renseigner le type du mouvement");
        }
        return errors;
    }
}

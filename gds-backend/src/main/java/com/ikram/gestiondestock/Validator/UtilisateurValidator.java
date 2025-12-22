package com.ikram.gestiondestock.Validator;

import com.ikram.gestiondestock.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurValidator {
    public static List<String> validate(UtilisateurDto utilisateurDto){
        List<String> errors = new ArrayList<>();
        if (utilisateurDto == null){
            errors.add("Veuillez renseigner le nom d'utilisateur");
            errors.add("Veuillez renseigner le prénom ");
            errors.add("Veuillez renseigner l'email");
            errors.add("Veuillez renseigner le mot de passe");
            errors.add("Veuillez renseigner l'adresse");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
        if (!StringUtils.hasLength(utilisateurDto.getNom())){
            errors.add("Veuillez renseigner le nom d'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getPrenom())){
            errors.add("Veuillez renseigner le prénom ");
        }
        if (!StringUtils.hasLength(utilisateurDto.getEmail())){
            errors.add("Veuillez renseigner l'email");
        }
        if (!StringUtils.hasLength(utilisateurDto.getMotDePasse())){
            errors.add("Veuillez renseigner le mot de passe");
        }
        if (utilisateurDto.getDateDeNaissance() == null){
            errors.add("Veuillez renseigner la date de naissance");
        }
        if (utilisateurDto.getAdresse() == null){
            errors.add("Veuillez renseigner l'adresse");
        }
        errors.addAll(AdresseValidator.validate(utilisateurDto.getAdresse()));
        return errors;
    }
}

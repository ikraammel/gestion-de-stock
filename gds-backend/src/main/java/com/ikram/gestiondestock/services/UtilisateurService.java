package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.ikram.gestiondestock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurDto save(UtilisateurDto utilisateurDto);
    UtilisateurDto findById(Integer id);
    UtilisateurDto findByEmail(String email);
    List<UtilisateurDto> findAll();
    void delete(Integer id);
    UtilisateurDto changerMotDePasse(String email,ChangerMotDePasseUtilisateurDto dto);
}

package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.EntrepriseDto;
import com.ikram.gestiondestock.dto.FournisseurDto;

import java.util.List;

public interface FournisseurService {
    FournisseurDto save(FournisseurDto fournisseurDto);
    FournisseurDto findById(Integer id);
    List<FournisseurDto> findAll();
    void delete(Integer id);
}

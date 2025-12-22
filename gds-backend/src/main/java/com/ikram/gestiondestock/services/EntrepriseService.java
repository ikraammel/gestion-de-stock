package com.ikram.gestiondestock.services;

import com.ikram.gestiondestock.dto.EntrepriseDto;

import java.util.List;

public interface EntrepriseService {
    EntrepriseDto save(EntrepriseDto entrepriseDto);
    EntrepriseDto findById(Integer id);
    List<EntrepriseDto> findAll();
    void delete(Integer id);
}

package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.EntrepriseDto;
import com.ikram.gestiondestock.services.impl.IEntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT +"/entreprise")
public class EntrepriseController {

    @Autowired
    private IEntrepriseService entrepriseService;

    @PostMapping("/create")
    public EntrepriseDto save(@RequestBody EntrepriseDto dto) {
        return entrepriseService.save(dto);
    }

    @GetMapping("/{id}")
    public EntrepriseDto findById(@PathVariable Integer id) {
        return entrepriseService.findById(id);
    }

    @GetMapping("/all")
    public List<EntrepriseDto> findAll() {
        return entrepriseService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        entrepriseService.delete(id);
    }
}

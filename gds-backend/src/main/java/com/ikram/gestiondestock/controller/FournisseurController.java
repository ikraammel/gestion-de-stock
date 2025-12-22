package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.FournisseurDto;
import com.ikram.gestiondestock.services.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @PostMapping("/create")
    public FournisseurDto save(@RequestBody FournisseurDto dto) {
        return fournisseurService.save(dto);
    }

    @GetMapping("/{id}")
    public FournisseurDto findById(@PathVariable Integer id) {
        return fournisseurService.findById(id);
    }

    @GetMapping("/all")
    public List<FournisseurDto> findAll() {
        return fournisseurService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        fournisseurService.delete(id);
    }
}

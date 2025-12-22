package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.VentesDto;
import com.ikram.gestiondestock.services.impl.IVentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT +"/ventes")
public class VentesController {

    @Autowired
    private IVentesService ventesService;

    @PostMapping("/create")
    VentesDto save(@RequestBody VentesDto ventesDto){
        return ventesService.save(ventesDto);
    }

    @GetMapping("/{id}")
    VentesDto findById(@PathVariable Integer id){
        return ventesService.findById(id);
    }

    @GetMapping("/code/{code}")
    VentesDto findByCode(@PathVariable String code){
        return ventesService.findByCode(code);
    }

    @GetMapping("/all")
    List<VentesDto> findAll(){
        return ventesService.findAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        ventesService.delete(id);
    }
}

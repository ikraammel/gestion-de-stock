package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.ClientDto;
import com.ikram.gestiondestock.services.impl.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/clients")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @PostMapping("/create")
    ClientDto save(@RequestBody ClientDto dto){
        return clientService.save(dto);
    }

    @GetMapping("/{id}")
    ClientDto findById(@PathVariable Integer id){
        return clientService.findById(id);
    }

    @GetMapping("/all")
    List<ClientDto> findAll(){
        return clientService.findAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        clientService.delete(id);
    }
}

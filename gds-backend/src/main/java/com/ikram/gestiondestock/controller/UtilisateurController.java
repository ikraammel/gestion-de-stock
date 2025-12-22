package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.ikram.gestiondestock.dto.UtilisateurDto;
import com.ikram.gestiondestock.services.impl.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT +"/utilisateurs")
public class UtilisateurController {

    @Autowired
    private IUtilisateurService utilisateurService;

    @PostMapping("/create")
    public UtilisateurDto save(@RequestBody UtilisateurDto dto) {
        return utilisateurService.save(dto);
    }

    @GetMapping("/{id}")
    public UtilisateurDto findById(@PathVariable Integer id) {
        return utilisateurService.findById(id);
    }

    @GetMapping("/email/{email}")
    public UtilisateurDto findByEmail(@PathVariable String email) {
        return utilisateurService.findByEmail(email);
    }

    @GetMapping("/all")
    public List<UtilisateurDto> findAll() {
        return utilisateurService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        utilisateurService.delete(id);
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> changerMotDePasse(
            @RequestBody ChangerMotDePasseUtilisateurDto dto,
            Principal principal
    ) {
        utilisateurService.changerMotDePasse(principal.getName(), dto);
        return ResponseEntity.ok().build();
    }

}

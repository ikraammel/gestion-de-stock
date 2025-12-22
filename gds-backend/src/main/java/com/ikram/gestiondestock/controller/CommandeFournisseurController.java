package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.CommandeFournisseurDto;
import com.ikram.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.ikram.gestiondestock.model.EtatCommande;
import com.ikram.gestiondestock.services.CommandeFournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT +"/commandesFournisseurs")
public class CommandeFournisseurController {
    @Autowired
    private CommandeFournisseurService commandeFournisseurService;

    @PostMapping("/create")
    public ResponseEntity<CommandeFournisseurDto> save(@RequestBody CommandeFournisseurDto commandeFournisseurDto){
        return ResponseEntity.ok(commandeFournisseurService.save(commandeFournisseurDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeFournisseurDto> findById(@PathVariable Integer id){
        return ResponseEntity.ok(commandeFournisseurService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CommandeFournisseurDto> findByCode(@PathVariable String code){
        return ResponseEntity.ok(commandeFournisseurService.findByCode(code));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommandeFournisseurDto>> findAll(){
        return ResponseEntity.ok(commandeFournisseurService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        commandeFournisseurService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/etat/{idCommande}/{etatCommande}")
    public ResponseEntity<CommandeFournisseurDto> updateEtatCommande(@PathVariable Integer idCommande,
                                                                @PathVariable EtatCommande etatCommande){
        return ResponseEntity.ok(commandeFournisseurService.updateEtatCommande(idCommande, etatCommande));
    }

    @PatchMapping("/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    public ResponseEntity<CommandeFournisseurDto> updateQuantiteCommande(@PathVariable Integer idCommande,
                                                                    @PathVariable Integer idLigneCommande,
                                                                    @PathVariable BigDecimal quantite){
        return ResponseEntity.ok(commandeFournisseurService.updateQuantiteCommande(idCommande, idLigneCommande, quantite));
    }

    @PatchMapping("/update/fournisseur/{idCommande}/{idFournisseur}")
    public ResponseEntity<CommandeFournisseurDto> updateFournisseur(@PathVariable Integer idCommande,
                                                          @PathVariable Integer idFournisseur){
        return ResponseEntity.ok(commandeFournisseurService.updateFournisseur(idCommande, idFournisseur));
    }

    @PatchMapping("/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    public ResponseEntity<CommandeFournisseurDto> updateArticle(@PathVariable Integer idCommande,
                                                           @PathVariable Integer idLigneCommande,
                                                           @PathVariable Integer idArticle){
        return ResponseEntity.ok(commandeFournisseurService.updateArticle(idCommande, idLigneCommande, idArticle));
    }

    @DeleteMapping("/delete/article/{idCommande}/{idLigneCommande}")
    public ResponseEntity<CommandeFournisseurDto> deleteArticle(@PathVariable Integer idCommande,
                                                           @PathVariable Integer idLigneCommande){
        return ResponseEntity.ok(commandeFournisseurService.deleteArticle(idCommande, idLigneCommande));
    }

    @GetMapping("/lignesCommande/{idCommande}")
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findAllLignesCommandesFournisseurByCommandeFournisseurId(
            @PathVariable Integer idCommande) {
        return ResponseEntity.ok(commandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande));
    }

}

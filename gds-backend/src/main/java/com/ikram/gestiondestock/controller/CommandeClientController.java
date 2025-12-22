package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.CommandeClientDto;
import com.ikram.gestiondestock.dto.LigneCommandeClientDto;
import com.ikram.gestiondestock.model.EtatCommande;
import com.ikram.gestiondestock.services.CommandeClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT +"/commandesClients")
public class CommandeClientController {

    @Autowired
    private CommandeClientService commandeClientService;

    @PostMapping("/create")
    public ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto commandeClientDto){
        return ResponseEntity.ok(commandeClientService.save(commandeClientDto));
    }

    @PatchMapping("/update/etat/{idCommande}/{etatCommande}")
    public ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable Integer idCommande,
                                                                @PathVariable EtatCommande etatCommande){
        return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
    }

    @PatchMapping("/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    public ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable Integer idCommande,
                                                                    @PathVariable Integer idLigneCommande,
                                                                @PathVariable BigDecimal quantite){
        return ResponseEntity.ok(commandeClientService.updateQuantiteCommande(idCommande, idLigneCommande, quantite));
    }

    @PatchMapping("/update/client/{idCommande}/{idClient}")
    public ResponseEntity<CommandeClientDto> updateClient(@PathVariable Integer idCommande,
                                                          @PathVariable Integer idClient){
        return ResponseEntity.ok(commandeClientService.updateClient(idCommande, idClient));
    }

    @PatchMapping("/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    public ResponseEntity<CommandeClientDto> updateArticle(@PathVariable Integer idCommande,
                                                           @PathVariable Integer idLigneCommande,
                                                           @PathVariable Integer idArticle){
        return ResponseEntity.ok(commandeClientService.updateArticle(idCommande, idLigneCommande, idArticle));
    }

    @DeleteMapping("/delete/article/{idCommande}/{idLigneCommande}")
    public ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable Integer idCommande,
                                                           @PathVariable Integer idLigneCommande){
        return ResponseEntity.ok(commandeClientService.deleteArticle(idCommande, idLigneCommande));
    }

    @GetMapping("/lignesCommande/{idCommande}")
    public ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable Integer idCommande) {
        return ResponseEntity.ok(commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeClientDto> findById(@PathVariable Integer id){
        return ResponseEntity.ok(commandeClientService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CommandeClientDto> findByCode(@PathVariable String code){
        return ResponseEntity.ok(commandeClientService.findByCode(code));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommandeClientDto>> findAll(){
        return ResponseEntity.ok(commandeClientService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        commandeClientService.delete(id);
        return ResponseEntity.ok().build();
    }
}

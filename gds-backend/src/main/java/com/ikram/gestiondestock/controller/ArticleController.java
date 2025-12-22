package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.ArticleDto;
import com.ikram.gestiondestock.dto.LigneCommandeClientDto;
import com.ikram.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.ikram.gestiondestock.dto.LigneVenteDto;
import com.ikram.gestiondestock.services.impl.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;
@AllArgsConstructor
@RestController
@RequestMapping(APP_ROOT + "/articles")
@Tag(name = "Articles",description = "Gestion des articles")
public class ArticleController {

    private final IArticleService articleService;

    @PostMapping("/create")
    @Operation(summary = "Créer un article", description = "Ajoute un nouvel article dans le stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Article créé avec succès"),
            @ApiResponse(responseCode = "400",description = "L'objet Article n'est pas valide")
    })
    ArticleDto save(@RequestBody ArticleDto articleDto){
        return articleService.save(articleDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Rechercher un article", description = "Rechercher un article par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Article trouvé dans la bd"),
            @ApiResponse(responseCode = "404",description = "Aucun Article trouvé dans la bd avec l'id fourni")
    })
    ArticleDto findById(@PathVariable Integer id){
        return articleService.findById(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Rechercher un article", description = "Rechercher un article par son code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Article trouvé dans la bd"),
            @ApiResponse(responseCode = "404",description = "Aucun Article trouvé dans la bd avec le code fourni")
    })
    ArticleDto findByCodeArticle(@PathVariable String code){
        return articleService.findByCodeArticle(code);
    }

    @GetMapping("/all")
    @Operation(summary = "Rechercher un article", description = "Renvoie la liste des articles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "La liste des articles")
    })
    List<ArticleDto> findAll(){
        return articleService.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un article", description = "Permer de supprimer un article par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "L'article a été supprimé avec succès")
    })
    void delete(@PathVariable Integer id){
        articleService.delete(id);
    }

    @GetMapping("/historique/vente/{idArticle}")
    List<LigneVenteDto> findHistoriqueVentes(@PathVariable Integer idArticle){
        return articleService.findHistoriqueVentes(idArticle);
    }

    @GetMapping("/historique/commandesClients/{idArticle}")
    List<LigneCommandeClientDto> findHistoriqueCommandesClients(@PathVariable Integer idArticle){
        return articleService.findHistoriqueCommandesClients(idArticle);
    }

    @GetMapping("/historique/commandesFournisseurs/{idArticle}")
    List<LigneCommandeFournisseurDto> findHistoriqueCommandesFournisseurs(@PathVariable Integer idArticle){
        return articleService.findHistoriqueCommandesFournisseurs(idArticle);
    }

    @GetMapping("/category/{idCategory}")
    List<ArticleDto> findAllArticlesByIdCategory(@PathVariable Integer idCategory){
        return articleService.findAllArticlesByIdCategory(idCategory);
    }
}

package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.dto.CategoryDto;
import com.ikram.gestiondestock.services.impl.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/categories")
@Tag(name = "Catégories", description = "Gestion des catégories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "Créer une catégorie", description = "Ajoute une nouvelle catégorie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "L'objet Category n'est pas valide")
    })
    public CategoryDto save(@RequestBody CategoryDto dto) {
        return categoryService.save(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Rechercher une catégorie", description = "Recherche une catégorie par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucune catégorie trouvée avec l'ID fourni")
    })
    public CategoryDto findById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @GetMapping("/code/{codeCategory}")
    @Operation(summary = "Rechercher une catégorie", description = "Recherche une catégorie par son code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucune catégorie trouvée avec le code fourni")
    })
    public CategoryDto findByCode(@PathVariable String codeCategory) {
        return categoryService.findByCode(codeCategory);
    }

    @GetMapping("/all")
    @Operation(summary = "Lister toutes les catégories", description = "Renvoie la liste de toutes les catégories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des catégories récupérée avec succès")
    })
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie", description = "Supprime une catégorie par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie supprimée avec succès")
    })
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}

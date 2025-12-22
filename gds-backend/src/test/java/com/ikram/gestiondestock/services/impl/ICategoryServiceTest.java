package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.dto.CategoryDto;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ICategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void shouldSaveCategoryWithSuccess(){
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Cat test")
                .designation("designation test")
                .entrepriseId(1)
                .build();
        CategoryDto savedCategory = categoryService.save(expectedCategory);

        Assertions.assertNotNull(savedCategory);
        Assertions.assertNotNull(savedCategory.getId());
        Assertions.assertEquals(expectedCategory.getCode(),savedCategory.getCode());
        Assertions.assertEquals(expectedCategory.getDesignation(),savedCategory.getDesignation());
        Assertions.assertEquals(expectedCategory.getEntrepriseId(),savedCategory.getEntrepriseId());
    }

    @Test
    public void shouldUpdateCategoryWithSuccess(){
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Cat test")
                .designation("designation test")
                .entrepriseId(1)
                .build();
        CategoryDto savedCategory = categoryService.save(expectedCategory);

        CategoryDto categoryToUpdate = savedCategory;
        categoryToUpdate.setCode("Cat update");

        savedCategory = categoryService.save(categoryToUpdate);

        Assertions.assertNotNull(categoryToUpdate);
        Assertions.assertNotNull(categoryToUpdate.getId());
        Assertions.assertEquals(categoryToUpdate.getCode(),savedCategory.getCode());
        Assertions.assertEquals(categoryToUpdate.getDesignation(),savedCategory.getDesignation());
        Assertions.assertEquals(categoryToUpdate.getEntrepriseId(),savedCategory.getEntrepriseId());
    }

    @Test
    public void shouldThrowInvalidEntityException(){
        CategoryDto expectedCategory = CategoryDto.builder()
                .build();

        InvalidEntityException expectedException = Assertions.assertThrows(InvalidEntityException.class,() -> categoryService.save(expectedCategory));
        Assertions.assertEquals(ErrorCodes.CATEGORY_NOT_VALID,expectedException.getErrorCodes());
        Assertions.assertEquals(1,expectedException.getErrors().size());
        Assertions.assertEquals("Veuillez renseigner le code de la catégorie",expectedException.getErrors().get(0));
    }

    @Test
    public void shouldThrowInvalidEntityNotFoundException(){
        EntityNotFoundException expectedException = Assertions.assertThrows(EntityNotFoundException.class,() -> categoryService.findById(0));
        Assertions.assertEquals(ErrorCodes.CATEGORY_NOT_FOUND,expectedException.getErrorCodes());
        Assertions.assertEquals("Aucune categorie avec l'ID = 0 n' ete trouve dans la BDD",expectedException.getMessage());
    }

}
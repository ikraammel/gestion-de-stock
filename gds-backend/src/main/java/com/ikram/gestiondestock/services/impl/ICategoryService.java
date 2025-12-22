package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.CategoryValidator;
import com.ikram.gestiondestock.dto.CategoryDto;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.model.Article;
import com.ikram.gestiondestock.model.Category;
import com.ikram.gestiondestock.repository.ArticleRepository;
import com.ikram.gestiondestock.repository.CategoryRepository;
import com.ikram.gestiondestock.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ICategoryService implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Category is not valid {}", dto);
            throw new InvalidEntityException(
                    "La category n'est pas valide",
                    ErrorCodes.CATEGORY_NOT_VALID,
                    errors
            );
        }

        if (dto.getId() != null) {
            // Update
            Category existing = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune category avec l'ID = " + dto.getId(),
                            ErrorCodes.CATEGORY_NOT_FOUND
                    ));

            // Mettre à jour les champs
            existing.setCode(dto.getCode());
            existing.setDesignation(dto.getDesignation());
            existing.setEntrepriseId(dto.getEntrepriseId());

            return CategoryDto.fromEntity(categoryRepository.save(existing));
        } else {
            // Create
            Category newCategory = CategoryDto.toEntity(dto);
            return CategoryDto.fromEntity(categoryRepository.save(newCategory));
        }
    }


    @Override
    public CategoryDto findById(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return null;
        }
        return categoryRepository.findById(id)
                .map(CategoryDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune categorie avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.CATEGORY_NOT_FOUND)
                );
    }

    @Override
    public CategoryDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Category CODE is null");
            return null;
        }
        return categoryRepository.findCategoryByCode(code)
                .map(CategoryDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune category avec le CODE = " + code + " n' ete trouve dans la BDD",
                        ErrorCodes.CATEGORY_NOT_FOUND)
                );
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return;
        }
        List<Article> articles = articleRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer cette categorie qui est deja utilisée",
                    ErrorCodes.CATEGORY_ALREADY_IN_USE);
        }

        categoryRepository.deleteById(id);
    }
}

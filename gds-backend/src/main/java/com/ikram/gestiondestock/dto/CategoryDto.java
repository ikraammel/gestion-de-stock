package com.ikram.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikram.gestiondestock.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class CategoryDto {
    private Integer id;
    private String code;
    private String designation;
    @JsonIgnore
    private List<ArticleDto> articles;
    private Integer entrepriseId;

    public static CategoryDto fromEntity(Category category){
        if (category == null){
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .designation(category.getDesignation())
                .entrepriseId(category.getEntrepriseId())
                .build();
    }

    public static Category toEntity(CategoryDto categoryDto){
        if (categoryDto == null){
            return null;
        }
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setCode(categoryDto.getCode());
        category.setDesignation(categoryDto.getDesignation());
        category.setEntrepriseId(categoryDto.getEntrepriseId());
        return category;
    }
}

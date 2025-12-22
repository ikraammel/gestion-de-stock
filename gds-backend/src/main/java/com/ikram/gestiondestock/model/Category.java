package com.ikram.gestiondestock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Category extends AbstractEntity{

    private String code;
    private String designation;

    @OneToMany(mappedBy = "category")
    private List<Article> articles;

    private Integer entrepriseId;

}

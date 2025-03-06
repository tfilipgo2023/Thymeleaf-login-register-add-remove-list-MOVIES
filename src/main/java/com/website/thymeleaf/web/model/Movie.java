package com.website.thymeleaf.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório.")
    private String titulo;

    @NotBlank(message = "Autor é obrigatório.")
    private String autor;

    @NotBlank(message = "Categoria é obrigatória.")
    private String categoria;

    @NotBlank(message = "Duração é obrigatória.")
    private String duracao;

    @NotBlank(message = "Editora é obrigatória.")
    private String editora;

    @NotBlank(message = "Ano de lançamento é obrigatório.")
    private String anoLancamento;

}

package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SugestaoDTO {
    private Integer quantidadeNumeros;
    private List<Integer> numeros;
    private String garantia;
    private Double probabilidade;
}

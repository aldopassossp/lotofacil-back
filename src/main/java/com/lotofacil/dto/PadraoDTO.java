package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PadraoDTO {
    private String tipo;
    private String valor;
    private Integer frequencia;
    private Double percentual;
}

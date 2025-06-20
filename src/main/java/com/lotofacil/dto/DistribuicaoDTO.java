package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistribuicaoDTO {
    private Integer pares;
    private Integer impares;
    private Integer primos;
    private Integer naoprimos;
    private Integer soma;
    private String distribuicaoLinhas;
    private String distribuicaoColunas;
}

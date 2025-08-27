package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtrasoNumeroDTO {

    private Integer idAtraso;   // era "numero" ou "posição"
    private Integer contagem;   // quantidade de atrasos
    private String ultimo;      // data do último sorteio
}

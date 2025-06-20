package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequenciaNumeroDTO {
    private Integer numero;
    private Integer frequencia;
    private Double percentual;
}

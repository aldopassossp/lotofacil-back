package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtrasoNumeroDTO {
    private Integer numero;
    private Integer concursosAtraso;
    private String ultimoSorteio;
}

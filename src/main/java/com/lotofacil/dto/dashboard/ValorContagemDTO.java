package com.lotofacil.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO base para estatísticas que envolvem um valor e sua contagem/frequência
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValorContagemDTO {
    private String valor; // Pode ser número, linha, coluna, sequência, etc.
    private Long contagem;
}

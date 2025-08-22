package com.lotofacil.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContagemLinhaDTO {
    private String linha; // Pode ser número, linha, coluna, sequência, etc.
    private Long ocorrencia;
}

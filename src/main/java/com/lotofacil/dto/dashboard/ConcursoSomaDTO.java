package com.lotofacil.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para representar a soma de uma combinação em um concurso específico
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcursoSomaDTO {
    private Long id_sorteados;
    private String dataSorteio;
    private Integer soma;
}

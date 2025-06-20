package com.lotofacil.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para representar a contagem de ímpares em um concurso específico
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcursoImparesDTO {
    private Long id_sorteados;
    private String dataSorteio;
    private Integer impares;
}

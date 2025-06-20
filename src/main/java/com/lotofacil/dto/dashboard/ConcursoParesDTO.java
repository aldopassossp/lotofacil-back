package com.lotofacil.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para representar a contagem de pares em um concurso específico
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcursoParesDTO {
    private Long id_sorteados;
    private String dataSorteio;
    private Integer pares;
}

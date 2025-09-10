package com.lotofacil.dto.sugestao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO para receber os filtros de sugestão do frontend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroSugestaoDTO {

    // Filtros de soma
    private Integer somaMinima;
    private Integer somaMaxima;

    // Filtros de pares/ímpares
    private Integer paresMinimo;
    private Integer paresMaximo;

    // Filtros de sequências
    private Integer seqDoisMinimo;
    private Integer seqDoisMaximo;
    private Integer seqTresMinimo;
    private Integer seqTresMaximo;

    // Filtros de pontos
    private Integer pontosMinimo;
    private Integer pontosMaximo;

    // Filtros de linha/coluna
    private List<String> linhasSelecionadas;
    private List<String> colunasSelecionadas;

    // Filtro de já foi sorteado
    private Boolean jaFoiSorteado;

    // Filtros de números específicos
    private List<Integer> numerosObrigatorios;
    private List<Integer> numerosProibidos;

    // Paginação
    private int page = 0;
    private int size = 50;
}


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
    private Integer imparesMinimo;
    private Integer imparesMaximo;

    // Filtros de sequências
    private Integer seqDoisMinimo;
    private Integer seqDoisMaximo;
    private Integer seqTresMinimo;
    private Integer seqTresMaximo;
    private Integer seqQuatroMinimo;
    private Integer seqQuatroMaximo;
    private Integer seqCincoMinimo;
    private Integer seqCincoMaximo;
    private Integer seqSeisMinimo;
    private Integer seqSeisMaximo;
    private Integer seqSeteMinimo;
    private Integer seqSeteMaximo;
    private Integer seqOitoMinimo;
    private Integer seqOitoMaximo;

    // Filtros de pontos
    private Integer pontosMinimo;
    private Integer pontosMaximo;

    // Filtros de linha/coluna
    private Integer linhaMinimo;
    private Integer linhaMaximo;
    private Integer colunaMinimo;
    private Integer colunaMaximo;

    // Filtro de já foi sorteado
    private Boolean jaFoiSorteado;

    // Filtros de números específicos
    private List<Integer> numerosObrigatorios;
    private List<Integer> numerosProibidos;

    // Paginação
    private int page = 0;
    private int size = 50;
}


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

    // Faixas numéricas (min/max)
    private Integer pontosMin;
    private Integer pontosMax;

    private Integer somaMin;
    private Integer somaMax;

    private Integer imparesMin;
    private Integer imparesMax;

    private Integer seqDoisMin;
    private Integer seqDoisMax;

    private Integer seqTresMin;
    private Integer seqTresMax;

    private Integer seqQuatroMin;
    private Integer seqQuatroMax;
    
    private Integer seqCincoMin;
    private Integer seqCincoMax;
    
    private Integer seqSeisMin;
    private Integer seqSeisMax;
    
    private Integer seqSeteMin;
    private Integer seqSeteMax;
    
    private Integer seqOitoMin;
    private Integer seqOitoMax;

    // Listas de valores exatos
    private List<String> linhas; // Lista de padrões de linha (ex: "55500")
    private List<String> colunas; // Lista de padrões de coluna (ex: "33333")

    // Outros filtros possíveis
    private Boolean naoIncluirSorteadosAnteriormente; // Flag para excluir combinações já sorteadas (todos.sorteado = 0)

    // Paginação e Limite
    private int page = 0; // Número da página (default 0)
    private int size = 20; // Tamanho da página (default 20)
}

package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SorteioDTO {
    private Integer id_sorteados;
    private List<Integer> numerosSorteados;
    private String dataDoSorteio;
}

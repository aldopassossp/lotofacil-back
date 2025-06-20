package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaDTO {
    private List<FrequenciaNumeroDTO> frequenciaNumeros;
    private List<AtrasoNumeroDTO> atrasoNumeros;
    private DistribuicaoDTO distribuicao;
    private List<PadraoDTO> padroes;
}

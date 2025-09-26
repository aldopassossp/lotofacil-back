package com.lotofacil.dto.request;

import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import lombok.Data;

@Data
public class GerarCombinacoesRequest {
    private FiltroSugestaoDTO filtros; // filtros aplicados
    private int quantidade; // quantos jogos gerar
    private int tamanho;    // 15, 16 ou 17
}

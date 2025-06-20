package com.lotofacil.controller;

import com.lotofacil.dto.EstatisticaDTO;
import com.lotofacil.dto.SugestaoDTO;
import com.lotofacil.service.EstatisticaService;
import com.lotofacil.service.suggestion.SugestaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estatisticas")
@Tag(name = "Estatísticas", description = "API para análise estatística dos sorteios")
public class EstatisticaController {

    @Autowired
    private EstatisticaService estatisticaService;
    
    @Autowired
    private SugestaoService sugestaoService;
    
    @GetMapping
    @Operation(summary = "Obter estatísticas dos sorteios", description = "Retorna análises estatísticas completas dos sorteios")
    public ResponseEntity<EstatisticaDTO> obterEstatisticas() {
        return ResponseEntity.ok(estatisticaService.gerarEstatisticas());
    }
    
    @GetMapping("/sugestoes/fechamento17")
    @Operation(summary = "Obter sugestões com fechamento de 17 números", description = "Retorna sugestões de jogos com fechamento de 17 números garantindo mínimo de 14 acertos")
    public ResponseEntity<List<SugestaoDTO>> obterSugestoesFechamento17() {
        return ResponseEntity.ok(sugestaoService.gerarSugestoesFechamento17());
    }
    
    @GetMapping("/sugestoes/fechamento16")
    @Operation(summary = "Obter sugestões com fechamento de 16 números", description = "Retorna sugestões de jogos com fechamento de 16 números garantindo mínimo de 14 acertos")
    public ResponseEntity<List<SugestaoDTO>> obterSugestoesFechamento16() {
        return ResponseEntity.ok(sugestaoService.gerarSugestoesFechamento16());
    }
    
    @GetMapping("/sugestoes/fechamento15")
    @Operation(summary = "Obter sugestões com fechamento de 15 números", description = "Retorna sugestões de jogos com 15 números")
    public ResponseEntity<List<SugestaoDTO>> obterSugestoesFechamento15() {
        return ResponseEntity.ok(sugestaoService.gerarSugestoesFechamento15());
    }
}

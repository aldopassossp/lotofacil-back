package com.lotofacil.controller;

import com.lotofacil.dto.request.GerarCombinacoesRequest;
import com.lotofacil.entity.HistoricoSugestao;
import com.lotofacil.repository.HistoricoSugestaoRepository;
import com.lotofacil.service.GeradorCombinacoesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sugestoes")
@RequiredArgsConstructor
public class GeradorCombinacoesController {

    private final GeradorCombinacoesService service;

    private final HistoricoSugestaoRepository historicoSugestaoRepository;

    @PostMapping("/gerar-combinacoes")
    public List<List<Integer>> gerar(@RequestBody GerarCombinacoesRequest request) {
        return service.gerarCombinacoes(request);
    }

    @PostMapping("/salvar-sugestoes")
    public ResponseEntity<Void> salvarSugestoes(@RequestBody List<HistoricoSugestao> sugestoes) {
        historicoSugestaoRepository.saveAll(sugestoes);
        return ResponseEntity.ok().build();
    }

}


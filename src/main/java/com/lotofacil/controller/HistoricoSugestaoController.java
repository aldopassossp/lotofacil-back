package com.lotofacil.controller;

import com.lotofacil.entity.HistoricoSugestao;
import com.lotofacil.repository.HistoricoSugestaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
public class HistoricoSugestaoController {

    @Autowired
    private HistoricoSugestaoRepository historicoSugestaoRepository;

    // 🔹 Listagem paginada
    @GetMapping
    public Page<HistoricoSugestao> listarPaginado(Pageable pageable) {
        return historicoSugestaoRepository.findAll(pageable);
    }

    // 🔹 Listar tudo (sem paginação) - usado na exportação CSV
    @GetMapping("/todos")
    public List<HistoricoSugestao> listarTodos() {
        return historicoSugestaoRepository.findAll();
    }

    // 🔹 Excluir um item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (historicoSugestaoRepository.existsById(id)) {
            historicoSugestaoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 🔹 Limpar todo o histórico
    @DeleteMapping("/limpar")
    public ResponseEntity<Void> limpar() {
        historicoSugestaoRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}

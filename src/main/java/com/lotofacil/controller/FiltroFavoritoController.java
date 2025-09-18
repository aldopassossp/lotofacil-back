package com.lotofacil.controller;

import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import com.lotofacil.entity.FiltroFavorito;
import com.lotofacil.service.FiltroFavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filtros-favoritos")
@RequiredArgsConstructor
public class FiltroFavoritoController {

    private final FiltroFavoritoService service;

    @PostMapping
    public FiltroFavorito salvar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam String nome,
            @RequestBody FiltroSugestaoDTO filtros) {
        return service.salvar(usuarioId, nome, filtros);
    }

    @GetMapping
    public List<FiltroFavorito> listar(@RequestParam(required = false) Long usuarioId) {
        return service.listar(usuarioId);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}")
    public FiltroSugestaoDTO carregar(@PathVariable Long id) {
        return service.carregar(id);
    }
}

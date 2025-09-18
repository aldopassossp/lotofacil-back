package com.lotofacil.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import com.lotofacil.entity.FiltroFavorito;
import com.lotofacil.repository.FiltroFavoritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FiltroFavoritoService {

    private final FiltroFavoritoRepository repository;
    private final ObjectMapper objectMapper; // do Jackson

    public FiltroFavorito salvar(Long usuarioId, String nome, FiltroSugestaoDTO filtros) {
        try {
            String filtrosJson = objectMapper.writeValueAsString(filtros);

            FiltroFavorito favorito = FiltroFavorito.builder()
                    .usuarioId(usuarioId)
                    .nome(nome)
                    .filtrosJson(filtrosJson)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

            return repository.save(favorito);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar filtros favoritos", e);
        }
    }

    public List<FiltroFavorito> listar(Long usuarioId) {
        if (usuarioId != null) {
            return repository.findByUsuarioId(usuarioId);
        }
        return repository.findAll();
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public FiltroSugestaoDTO carregar(Long id) {
        try {
            FiltroFavorito favorito = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Filtro não encontrado"));

            return objectMapper.readValue(favorito.getFiltrosJson(), FiltroSugestaoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar filtro favorito", e);
        }
    }
}

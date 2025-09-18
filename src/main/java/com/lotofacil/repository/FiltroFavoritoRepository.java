package com.lotofacil.repository;

import com.lotofacil.entity.FiltroFavorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FiltroFavoritoRepository extends JpaRepository<FiltroFavorito, Long> {
    // Se quiser buscar por usuário
    List<FiltroFavorito> findByUsuarioId(Long usuarioId);
}
